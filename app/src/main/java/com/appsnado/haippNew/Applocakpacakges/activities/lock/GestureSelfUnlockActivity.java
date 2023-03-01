package com.appsnado.haippNew.Applocakpacakges.activities.lock;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.appsnado.haippNew.Activities.GestureServicesact;
import com.appsnado.haippNew.Applocakpacakges.activities.setting.LockSettingActivity;
import com.appsnado.haippNew.Applocakpacakges.base.AppConstants;

import com.appsnado.haippNew.Applocakpacakges.db.CommLockInfoManager;
import com.appsnado.haippNew.Applocakpacakges.utils.LockPatternUtils;
import com.appsnado.haippNew.Applocakpacakges.utils.SpUtil;
import com.appsnado.haippNew.Applocakpacakges.utils.SystemBarHelper;
import com.appsnado.haippNew.Applocakpacakges.widget.LockPatternView;
import com.appsnado.haippNew.Applocakpacakges.widget.LockPatternViewPattern;
import com.appsnado.haippNew.DevAdminReceiver;
import com.appsnado.haippNew.MainActivity;
import com.appsnado.haippNew.R;
import com.appsnado.haippNew.baseactivity.BaseActivity;
import com.appsnado.haippNew.data.SharedPreferenceManager;

import java.util.List;

/**
 * Created by xian on 2017/2/17.
 */

public class GestureSelfUnlockActivity extends BaseActivity {

    private LockPatternView mLockPatternView;
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private int mFailedPatternAttemptsSinceLastTimeout = 0;
    private String actionFrom;
    private String pkgName;
    private CommLockInfoManager mManager;
    private RelativeLayout mTopLayout;

    private TextureView mTextureView;
    private SharedPreferenceManager preferenceManager;
    private boolean lockstate = false;
    @NonNull
    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_self_unlock);

        mLockPatternView = findViewById(R.id.unlock_lock_view);
        mTopLayout = findViewById(R.id.top_layout);
       // mTextureView = findViewById(R.id.texture_view);
        mTopLayout.setPadding(0, SystemBarHelper.getStatusBarHeight(this), 0, 0);


        mManager = new CommLockInfoManager(this);
        pkgName = getIntent().getStringExtra(AppConstants.LOCK_PACKAGE_NAME);
        actionFrom = getIntent().getStringExtra(AppConstants.LOCK_FROM);

        preferenceManager = new SharedPreferenceManager(GestureSelfUnlockActivity.this);

        initLockPatternView();

    }

    public int getLayoutId() {
        return R.layout.activity_gesture_self_unlock;
    }

    protected void initViews(Bundle savedInstanceState) {

    }

    protected void initData() {


    }

    private void initLockPatternView() {
        mLockPatternUtils = new LockPatternUtils(this);
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);
        mPatternViewPattern.setPatternListener(new LockPatternViewPattern.onPatternListener() {
            @Override
            public void onPatternDetected(@NonNull List<LockPatternView.Cell> pattern) {
                if (mLockPatternUtils.checkPattern(pattern)) {
                    mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                    if (actionFrom.equals(AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY)) {
                        lockstate = true;
                        Intent intent = new Intent(GestureSelfUnlockActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    } else if (actionFrom.equals(AppConstants.LOCK_FROM_FINISH)) {
                        mManager.unlockCommApplication(pkgName);
                        finish();
                    } else if (actionFrom.equals(AppConstants.LOCK_FROM_SETTING)) {
                        startActivity(new Intent(GestureSelfUnlockActivity.this, LockSettingActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    } else if (actionFrom.equals(AppConstants.LOCK_FROM_UNLOCK)) {
                        mManager.setIsUnLockThisApp(pkgName, true);
                        mManager.unlockCommApplication(pkgName);
                        sendBroadcast(new Intent(GestureServicesact.FINISH_UNLOCK_THIS_APP));
                        finish();
                    }
                } else {
                    mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                    if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
                        mFailedPatternAttemptsSinceLastTimeout++;
                        int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout;
                        if (retry >= 0) {
                        }
                    } else {

                    }
                    if (mFailedPatternAttemptsSinceLastTimeout >= 3) {
                        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_RECORD_PIC, false)) {

                        }
                    }
                    if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) { //The number of failures is greater than the maximum number of incorrect attempts before blocking the use

                    } else {
                        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
                    }
                }
            }
        });
        mLockPatternView.setOnPatternListener(mPatternViewPattern);
        mLockPatternView.setTactileFeedbackEnabled(true);
    }

    protected void initAction() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(preferenceManager.getscreenlock() == true){
            if(!lockstate) {
                DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                ComponentName mDeviceAdminRcvr = new ComponentName(this, DevAdminReceiver.class);
                boolean active = mDPM.isAdminActive(mDeviceAdminRcvr);
                if (active) {
                    mDPM.lockNow();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(preferenceManager.getscreenlock() == true){
            if(!lockstate) {
                DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                ComponentName mDeviceAdminRcvr = new ComponentName(this, DevAdminReceiver.class);
                boolean active = mDPM.isAdminActive(mDeviceAdminRcvr);
                if (active) {
                    mDPM.lockNow();
                }
            }
        }
    }
}
