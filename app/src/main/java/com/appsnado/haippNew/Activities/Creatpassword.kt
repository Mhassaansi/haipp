package com.appsnado.haippNew.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.appsnado.haippNew.Applocakpacakges.base.AppConstants
import com.appsnado.haippNew.Applocakpacakges.model.LockStage
import com.appsnado.haippNew.Applocakpacakges.mvp.contract.GestureCreateContract
import com.appsnado.haippNew.Applocakpacakges.mvp.p.GestureCreatePresenter
import com.appsnado.haippNew.Applocakpacakges.services.BackgroundManager
import com.appsnado.haippNew.Applocakpacakges.services.LockService
import com.appsnado.haippNew.Applocakpacakges.utils.LockPatternUtils
import com.appsnado.haippNew.Applocakpacakges.utils.SpUtil
import com.appsnado.haippNew.Applocakpacakges.utils.SystemBarHelper
import com.appsnado.haippNew.Applocakpacakges.widget.LockPatternView
import com.appsnado.haippNew.Applocakpacakges.widget.LockPatternViewPattern
import com.appsnado.haippNew.Applocakpacakges.widget.LockPatternViewPattern.onPatternListener
import com.appsnado.haippNew.MainActivity
import com.appsnado.haippNew.R

class Creatpassword : AppCompatActivity(), GestureCreateContract.View ,View.OnClickListener{

    private var mBtnReset: TextView? = null
    private var mUiStage = LockStage.Introduction
    private var mLockPatternUtils: LockPatternUtils? = null
    private var mPatternViewPattern: LockPatternViewPattern? = null
    private var mGestureCreatePresenter: GestureCreatePresenter? = null
    private var mTopLayout: RelativeLayout? = null
    private var mChosenPattern: List<LockPatternView.Cell>? = null
    private var mLockTip: TextView? = null
    private var mLockPatternView: LockPatternView? = null
    private val mClearPatternRunnable = Runnable { mLockPatternView!!.clearPattern() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pwd_lock)
         datainit()
    }

    private fun datainit() {

        mLockPatternView = findViewById<LockPatternView>(R.id.lock_pattern_view)
        mLockTip = findViewById<TextView>(R.id.lock_tip)
        mBtnReset = findViewById(R.id.btn_reset)
        mTopLayout = findViewById(R.id.top_layout)
        mTopLayout?.setPadding(0, SystemBarHelper.getStatusBarHeight(this), 0, 0)
        mGestureCreatePresenter = GestureCreatePresenter(this, this)
        mBtnReset?.setOnClickListener(this)
        initLockPatternView()
    }


    private fun initLockPatternView() {
        mLockPatternUtils = LockPatternUtils(this)
        mPatternViewPattern = LockPatternViewPattern(mLockPatternView)
        mPatternViewPattern!!.setPatternListener(onPatternListener { pattern ->
            mGestureCreatePresenter!!.onPatternDetected(
                pattern,
                mChosenPattern,
                mUiStage
            )
        })
        mLockPatternView?.setOnPatternListener(mPatternViewPattern)
        mLockPatternView?.setTactileFeedbackEnabled(true)
    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_reset -> setStepOne()
        }
    }
    private fun setStepOne() {
        mGestureCreatePresenter!!.updateStage(LockStage.Introduction)
        mLockTip!!.text = getString(R.string.lock_recording_intro_header)
    }





    override fun updateUiStage(stage: LockStage) {
        mUiStage = stage
    }

    override fun updateChosenPattern(mChosenPattern: List<LockPatternView.Cell>?) {
        this.mChosenPattern = mChosenPattern
    }

    override fun updateLockTip(text: String?, isToast: Boolean) {
        mLockTip!!.text = text
    }

    override fun setHeaderMessage(headerMessage: Int) {
        mLockTip!!.setText(headerMessage)
    }

    override fun lockPatternViewConfiguration(
        patternEnabled: Boolean,
        displayMode: LockPatternView.DisplayMode?) {
        if (patternEnabled) {
            mLockPatternView!!.enableInput()
        } else {
            mLockPatternView!!.disableInput()
        }
        mLockPatternView!!.setDisplayMode(displayMode)
    }

    override fun Introduction() {
        clearPattern()
    }

    override fun HelpScreen() {}

    override fun ChoiceTooShort() {
        mLockPatternView!!.setDisplayMode(LockPatternView.DisplayMode.Wrong)
        mLockPatternView!!.removeCallbacks(mClearPatternRunnable)
        mLockPatternView!!.postDelayed(mClearPatternRunnable, 500)
    }

    override fun moveToStatusTwo() {}


    override fun clearPattern() {
        mLockPatternView!!.clearPattern()
    }


    override fun ConfirmWrong() {
        mLockPatternView!!.setDisplayMode(LockPatternView.DisplayMode.Wrong)
        mLockPatternView!!.removeCallbacks(mClearPatternRunnable)
        mLockPatternView!!.postDelayed(mClearPatternRunnable, 500)
    }


    override fun ChoiceConfirmed() {
        mLockPatternUtils!!.saveLockPattern(mChosenPattern)
        clearPattern()
        gotoLockMainActivity()
    }

    private fun gotoLockMainActivity() {
        SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, true)
        BackgroundManager.getInstance().init(this).startService(LockService::class.java)
        SpUtil.getInstance().putBoolean(AppConstants.LOCK_IS_FIRST_LOCK, false)
        startActivity(
            Intent(
                this,
                MainActivity::class.java
            )
        )
        finish()
    }
}