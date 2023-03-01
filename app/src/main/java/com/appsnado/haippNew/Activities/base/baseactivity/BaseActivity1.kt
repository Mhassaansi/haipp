//package com.appsnado.haipp.baseactivity
//
//import android.os.Bundle
//import android.util.DisplayMetrics
//import androidx.annotation.IdRes
//import androidx.appcompat.app.AppCompatActivity
//import com.appsnado.haipp.Applocakpacakges.LockApplication
//import com.appsnado.haipp.MainActivity.Companion.navController
//import com.appsnado.haipp.R
//
//abstract class BaseActivity : AppCompatActivity() {
//   // var fragmentNavigator: FragmentNavigator? = null
//   // var fragmentNavigatorslidemenu: FragmentNavigator? = null
//   override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//          LockApplication.getInstance().doForCreate(this)
//            setContentView(getLayoutId())
//            initViews(savedInstanceState!!)
//
//
//
//       //  fragmentNavigator = FragmentNavigator()
//       //  fragmentNavigatorslidemenu = FragmentNavigator()
//
//
//
//
//       registerControllers()
//       register_slidemenu()
//       initData();
//      /// initAction()
//
//
//   }
//    protected abstract fun initData();
//    protected abstract fun initViews(savedInstanceState:Bundle);
//   // protected abstract fun initAction()
//
//    abstract fun getLayoutId(): Int
//    override fun onDestroy() {
//        super.onDestroy()
//        LockApplication.getInstance().doForFinish(this)
//        //fragmentNavigator!!.unRegister()
//    }
//
//
//
//    private fun registerControllers() {
//       //fragmentNavigator!!.register(supportFragmentManager, containerId)
//    }
//
//    private fun register_slidemenu() {
//       // fragmentNavigatorslidemenu!!.register(supportFragmentManager, containermenu_Id)
//    }
//
//    override fun onBackPressed() {
//
//
//
//
//        when (navController.currentDestination?.id) {
//             R.id.verificationFragment, R.id.roleFragment,R.id.loginFragment, R.id.homeFragment, R.id.featureFragment, R.id.settingFragment,R.id.chatFragment2 -> {
////                if (onBackPressedDispatcher.hasEnabledCallbacks())
////                    onBackPressedDispatcher.onBackPressed()
//               // else
//                  ///  navController.navigateUp()
//            }
//            else -> navController.navigateUp()
//        }
//
//
//
//
//
//
//      //  navController.popBackStack()
//    }
//
//    fun dpToPx(dp: Int): Int {
//        val displayMetrics = resources.displayMetrics
//        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
//    }
//
////    @get:IdRes
////    abstract val containerId: Int
////
////    @get:IdRes
////    abstract val containermenu_Id: Int
//
//    companion object {
//        const val KEY_FRAG_FIRST = "firstFrag"
//    }
//
//
//
//    fun clear() {
//        super.finish()
//    }
//}