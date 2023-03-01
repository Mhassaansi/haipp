package com.appsnado.haippNew.baseactivity

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.Applocakpacakges.LockApplication
import com.appsnado.haippNew.BrowserApp
import com.appsnado.haippNew.R

abstract class BaseActivity : AppCompatActivity() {
   // var fragmentNavigator: FragmentNavigator? = null
   // var fragmentNavigatorslidemenu: FragmentNavigator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       BrowserApp.getInstance()!!.doForCreate(this)
        //fragmentNavigator = FragmentNavigator()
       // fragmentNavigatorslidemenu = FragmentNavigator()
        registerControllers()
        register_slidemenu()
    }

    override fun onDestroy() {
        super.onDestroy()
        //fragmentNavigator!!.unRegister()
    }





    private fun registerControllers() {
       //fragmentNavigator!!.register(supportFragmentManager, containerId)
    }

    private fun register_slidemenu() {
       // fragmentNavigatorslidemenu!!.register(supportFragmentManager, containermenu_Id)
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
             R.id.verificationFragment, R.id.roleFragment,R.id.loginFragment, R.id.homeFragment, R.id.featureFragment, R.id.settingFragment,R.id.chatFragment2 -> {
//                if (onBackPressedDispatcher.hasEnabledCallbacks())
//                    onBackPressedDispatcher.onBackPressed()
               // else
                  ///  navController.navigateUp()
            }
            else -> navController.navigateUp()
        }






      //  navController.popBackStack()
    }

    fun dpToPx(dp: Int): Int {
        val displayMetrics = resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

//    @get:IdRes
//    abstract val containerId: Int
//
//    @get:IdRes
//    abstract val containermenu_Id: Int

    companion object {
        const val KEY_FRAG_FIRST = "firstFrag"
    }

    fun clear() {
        super.finish()
    }

}