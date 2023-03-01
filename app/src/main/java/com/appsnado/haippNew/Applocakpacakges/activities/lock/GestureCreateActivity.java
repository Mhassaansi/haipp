package com.appsnado.haippNew.Applocakpacakges.activities.lock;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



import com.appsnado.haippNew.Applocakpacakges.model.LockStage;
import com.appsnado.haippNew.Applocakpacakges.mvp.contract.GestureCreateContract;
import com.appsnado.haippNew.Applocakpacakges.mvp.p.GestureCreatePresenter;
import com.appsnado.haippNew.Applocakpacakges.utils.LockPatternUtils;
import com.appsnado.haippNew.Applocakpacakges.utils.SystemBarHelper;
import com.appsnado.haippNew.Applocakpacakges.utils.ToastUtil;
import com.appsnado.haippNew.Applocakpacakges.widget.LockPatternView;
import com.appsnado.haippNew.Applocakpacakges.widget.LockPatternViewPattern;
import com.appsnado.haippNew.R;
import com.appsnado.haippNew.baseactivity.BaseActivity;

import java.util.List;

/**
 * Created by xian on 2017/2/17.
 */

public class GestureCreateActivity extends BaseActivity implements View.OnClickListener, GestureCreateContract.View {

    private static final String KEY_PATTERN_CHOICE = "chosenPattern";
    private static final String KEY_UI_STAGE = "uiStage";
    @Nullable
    protected List<LockPatternView.Cell> mChosenPattern = null;
    private LockPatternView mLockPatternView;
    private TextView mLockTip;
    private LockStage mUiStage = LockStage.Introduction;
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private GestureCreatePresenter mGestureCreatePresenter;
    private RelativeLayout mTopLayout;

    @NonNull
    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };

    public int getLayoutId() {
        return R.layout.activity_gesture_lock;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_lock);
        mLockTip = findViewById(R.id.lock_tip);
        mLockPatternView = findViewById(R.id.lock_pattern_view);
        mTopLayout = findViewById(R.id.top_layout);
        mTopLayout.setPadding(0, SystemBarHelper.getStatusBarHeight(this), 0, 0);

        mGestureCreatePresenter = new GestureCreatePresenter(this, this);
        initLockPatternView();
        if (savedInstanceState == null) {
            mGestureCreatePresenter.updateStage(LockStage.Introduction);
        } else {
            final String patternString = savedInstanceState.getString(KEY_PATTERN_CHOICE);
            if (patternString != null) {
                mChosenPattern = LockPatternUtils.stringToPattern(patternString);
            }
            mGestureCreatePresenter.updateStage(LockStage.values()[savedInstanceState.getInt(KEY_UI_STAGE)]);
        }




        mLockPatternUtils = new LockPatternUtils(this);
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);
        mPatternViewPattern.setPatternListener(new LockPatternViewPattern.onPatternListener() {
            @Override
            public void onPatternDetected(@NonNull List<LockPatternView.Cell> pattern) {
                mGestureCreatePresenter.onPatternDetected(pattern, mChosenPattern, mUiStage);
            }
        });
            mLockPatternView.setOnPatternListener(mPatternViewPattern);
            mLockPatternView.setTactileFeedbackEnabled(true);
    }

    protected void initViews(@Nullable Bundle savedInstanceState) {

    }


    private void initLockPatternView() {

    }

    protected void initData() {

    }

    protected void initAction() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void updateUiStage(LockStage stage) {
        mUiStage = stage;
    }

    @Override
    public void updateChosenPattern(List<LockPatternView.Cell> mChosenPattern) {
        this.mChosenPattern = mChosenPattern;
    }

    @Override
    public void updateLockTip(String text, boolean isToast) {
        if (isToast) {
            ToastUtil.showToast(text);
        } else {
            mLockTip.setText(text);
        }
    }

    @Override
    public void setHeaderMessage(int headerMessage) {
        mLockTip.setText(headerMessage);
    }

    @Override
    public void lockPatternViewConfiguration(boolean patternEnabled, LockPatternView.DisplayMode displayMode) {
        if (patternEnabled) {
            mLockPatternView.enableInput();
        } else {
            mLockPatternView.disableInput();
        }
        mLockPatternView.setDisplayMode(displayMode);
    }

    @Override
    public void Introduction() {
        clearPattern();
    }

    @Override
    public void HelpScreen() {

    }

    @Override
    public void ChoiceTooShort() {
        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 1000);
    }

    @Override
    public void moveToStatusTwo() {

    }

    @Override
    public void clearPattern() {
        mLockPatternView.clearPattern();
    }

    @Override
    public void ConfirmWrong() {
        mChosenPattern = null;
        clearPattern();
    }

    @Override
    public void ChoiceConfirmed() {
        mLockPatternUtils.saveLockPattern(mChosenPattern);
        clearPattern();
        setResult(RESULT_OK);
        finish();
    }
}
