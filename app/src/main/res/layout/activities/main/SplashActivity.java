package com.appsnado.haipp.Applocakpacakges.activities.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.ImageView;


import com.appsnado.haipp.Applocakpacakges.Applocakpacakges.R;
import com.appsnado.haipp.Applocakpacakges.base.AppConstants;
import com.appsnado.haipp.Applocakpacakges.base.BaseActivity;
import com.appsnado.haipp.Applocakpacakges.activities.lock.GestureSelfUnlockActivity;
import com.appsnado.haipp.Applocakpacakges.activities.pwd.CreatePwdActivity;
import com.appsnado.haipp.Applocakpacakges.services.BackgroundManager;
import com.appsnado.haipp.Applocakpacakges.services.LoadAppListService;
import com.appsnado.haipp.Applocakpacakges.services.LockService;
import com.appsnado.haipp.Applocakpacakges.utils.AppUtils;
import com.appsnado.haipp.Applocakpacakges.utils.LockUtil;
import com.appsnado.haipp.Applocakpacakges.utils.SpUtil;
import com.appsnado.haipp.Applocakpacakges.utils.ToastUtil;
import com.appsnado.haipp.Applocakpacakges.widget.DialogPermission;

/**
 * Created by xian on 2017/2/17.
 */

public class SplashActivity extends BaseActivity {
    private static final int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 1;
    private static final int RESULT_ACTION_ACCESSIBILITY_SETTINGS = 3;

    private ImageView mImgSplash;
    @Nullable
    private ObjectAnimator animator;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash_lock;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        AppUtils.hideStatusBar(getWindow(), true);
        mImgSplash = findViewById(R.id.img_splash);
    }

    @Override
    protected void initData() {
        //startService(new Intent(this, LoadAppListService.class));
        BackgroundManager.getInstance().init(this).startService(LoadAppListService.class);

        //start lock services if  everything is already  setup
        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE, false)) {
            BackgroundManager.getInstance().init(this).startService(LockService.class);
        }

        animator = ObjectAnimator.ofFloat(mImgSplash, "alpha", 0.5f, 1);
        animator.setDuration(1500);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                boolean isFirstLock = SpUtil.getInstance().getBoolean(AppConstants.LOCK_IS_FIRST_LOCK, true);
                if (isFirstLock) {
                    showDialog();
                } else {
                    Intent intent = new Intent(SplashActivity.this, GestureSelfUnlockActivity.class);
                    intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, AppConstants.APP_PACKAGE_NAME);
                    intent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    private void showDialog() {
        // If you do not have access to view usage rights and the phone exists to view usage this interface
        if (!LockUtil.isStatAccessPermissionSet(SplashActivity.this) && LockUtil.isNoOption(SplashActivity.this)) {
            DialogPermission dialog = new DialogPermission(SplashActivity.this);
            dialog.show();
            dialog.setOnClickListener(new DialogPermission.onClickListener() {
                @Override
                public void onClick() {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        Intent intent = null;
                        intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivityForResult(intent, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
                    }
                }
            });
        } else {
            gotoCreatePwdActivity();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ACTION_USAGE_ACCESS_SETTINGS) {
            if (LockUtil.isStatAccessPermissionSet(SplashActivity.this)) {
                gotoCreatePwdActivity();
            } else {
                ToastUtil.showToast("Permission denied");
                finish();
            }
        }
        if (requestCode == RESULT_ACTION_ACCESSIBILITY_SETTINGS) {
            gotoCreatePwdActivity();
        }
    }

    public boolean isAccessibilityEnabled() {
        int accessibilityEnabled = 0;
        final String ACCESSIBILITY_SERVICE = "io.github.subhamtyagi.privacyapplock/com.lzx.lock.service.LockAccessibilityService";
        try {
            accessibilityEnabled = Settings.Secure.getInt(this.getContentResolver(), android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            //setting not found so your phone is not supported
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessabilityService = mStringColonSplitter.next();
                    if (accessabilityService.equalsIgnoreCase(ACCESSIBILITY_SERVICE)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void gotoCreatePwdActivity() {
        Intent intent2 = new Intent(SplashActivity.this, CreatePwdActivity.class);
        startActivity(intent2);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void initAction() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animator = null;
    }
}