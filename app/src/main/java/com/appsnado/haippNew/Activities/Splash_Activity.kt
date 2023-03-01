package com.appsnado.haippNew.Activities

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.AttributeSet
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.appsnado.haippNew.Activities.Creatpassword
import com.appsnado.haippNew.Activities.MainActivity
import com.appsnado.haippNew.Applocakpacakges.base.AppConstants
import com.appsnado.haippNew.Applocakpacakges.base.AppConstants.firebaseappstatus
import com.appsnado.haippNew.Applocakpacakges.services.BackgroundManager
import com.appsnado.haippNew.Applocakpacakges.services.LoadAppListService
import com.appsnado.haippNew.Applocakpacakges.services.LockService
import com.appsnado.haippNew.Applocakpacakges.utils.LockUtil
import com.appsnado.haippNew.Applocakpacakges.utils.SpUtil
import com.appsnado.haippNew.Applocakpacakges.utils.ToastUtil
import com.appsnado.haippNew.Applocakpacakges.widget.DialogPermission
import com.appsnado.haippNew.R
import com.appsnado.haippNew.data.SharedPreferenceManager
import com.appsnado.haippNew.databinding.Splash
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class Splash_Activity : Activity() {
     private var splashHandler: Handler? = null
    private var splashHandlertext: Handler? = null
     var imgAppLogo : ImageView? = null
     var binding : Splash? = null
     var preferenceManager: SharedPreferenceManager? = null
    private var animator: ObjectAnimator? = null
    private val RESULT_ACTION_USAGE_ACCESS_SETTINGS = 1
    private val RESULT_ACTION_ACCESSIBILITY_SETTINGS = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.TransparentStatusBarTheme)
       /// AppUtils.hideStatusBar(window, true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        preferenceManager = SharedPreferenceManager(this)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        splashHandler = Handler()
        splashHandlertext = Handler()

       //  initiateAnimation()
       //  initiateAnimationTex()


        preferenceManager!!.setfirsttimservices(true)

        if (checkPermissions()!!.size == 0) {

        } else {
            requestPermiss()
        }

        firebaseappstatus = true;

         calldata()
    }

    private fun requestPermiss() {

        val permissions = checkPermissions()
        if (permissions!!.size > 0) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 0)
        }
    }

    private fun checkPermissions(): List<String>? {
        val permissions: MutableList<String> =
            ArrayList()
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_PHONE_STATE)
        }
        return permissions
    }

    private fun calldata() {

        //startService(new Intent(this, LoadAppListService.class));
        BackgroundManager.getInstance().init(this).startService(LoadAppListService::class.java)

        //start lock services if  everything is already  setup

        //start lock services if  everything is already  setup
        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE, false)) {
            BackgroundManager.getInstance().init(this).startService(LockService::class.java)
        }

        animator = ObjectAnimator.ofFloat(binding?.imglogo, "alpha", 0.5f, 1f)
        animator?.setDuration(1500)
        animator?.start()
        animator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                val isFirstLock =
                    SpUtil.getInstance().getBoolean(AppConstants.LOCK_IS_FIRST_LOCK, true)
                if (isFirstLock) {
                    showDialog()
                } else {

                    startActivity(MainActivity.getCallingIntent(this@Splash_Activity))
                    finish()
//                    val intent =
//                        Intent(this@Splash_Activity, GestureSelfUnlockActivity::class.java)
//                    intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, AppConstants.APP_PACKAGE_NAME)
//                    intent.putExtra(
//                        AppConstants.LOCK_FROM,
//                        AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY
//                    )
//                    startActivity(intent)
//                    finish()
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)


                }
            }
        })
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)

    }
    private fun showDialog() {
        // If you do not have access to view usage rights and the phone exists to view usage this interface
        if (!LockUtil.isStatAccessPermissionSet(this@Splash_Activity) && LockUtil.isNoOption(this@Splash_Activity)) {
            val dialog = DialogPermission(this@Splash_Activity)
            dialog.show()
            dialog.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    var intent: Intent? = null
                    intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                    startActivityForResult(
                        intent,
                        RESULT_ACTION_USAGE_ACCESS_SETTINGS
                    )
                }
            }
        } else {
            gotoCreatePwdActivity()
        }
    }

    private fun gotoCreatePwdActivity() {
        val intent2 = Intent(this@Splash_Activity, Creatpassword::class.java)
        startActivity(intent2)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
    private fun initiateAnimation() {
        val animation = AnimationUtils.loadAnimation(this@Splash_Activity, R.anim.fade_in)
        binding?.imglogo!!.startAnimation(animation)
        splashHandler!!.postDelayed({
            recreatestat = true
          //  startActivity(MainActivity.getCallingIntent(this@Splash_Activity))
           // finish()
        }
            , SPLASH_DURATION)
    }
    private fun initiateAnimationTex() {
        val animation1 = AnimationUtils.loadAnimation(this@Splash_Activity, R.anim.fade_in)
        binding?.textview!!.startAnimation(animation1)
        splashHandlertext!!.postDelayed({
            recreatestat = true
            startActivity(MainActivity.getCallingIntent(this@Splash_Activity))
            finish()
        }
                , SPLASH_DURATION_Tex)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (splashHandler != null) {
            splashHandler!!.removeCallbacksAndMessages(null)
        }
    }

    fun printHashKey(pContext: Context) {
        try {
                val info = pContext.packageManager.getPackageInfo(
                pContext.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i(ContentValues.TAG, "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(ContentValues.TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "printHashKey()", e)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_ACTION_USAGE_ACCESS_SETTINGS) {
            if (LockUtil.isStatAccessPermissionSet(this)) {
                gotoCreatePwdActivity()
            } else {
                ToastUtil.showToast("Permission denied")
                finish()
            }
        }
        if (requestCode == RESULT_ACTION_ACCESSIBILITY_SETTINGS) {
            gotoCreatePwdActivity()
        }
    }

    companion object {
        private const val SPLASH_DURATION: Long = 4000
        private const val SPLASH_DURATION_Tex: Long = 6000
        @JvmField
        var recreatestat = false
    }
}