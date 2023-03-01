package com.appsnado.haippNew.Role

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.appsnado.haippNew.Activities.MainActivity.Companion.tokenfirebasedata
import com.appsnado.haippNew.Applocakpacakges.activities.lock.GestureSelfUnlockActivity
import com.appsnado.haippNew.Applocakpacakges.base.AppConstants
import com.appsnado.haippNew.Comman_Pacakges.data.UserWrapper
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.RoleBind
import com.appsnado.haippNew.retro.WebServiceConstants.userid
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class RoleFragment : BaseFragment<RoleViewModel>(),View.OnClickListener {
      lateinit var navController: NavController
      var binding: RoleBind? = null
    var cal_services : Boolean =false
    var token :String?= null
     companion object {
         fun newInstance() = RoleFragment()
      }

    private lateinit var roleviewModel: RoleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
        com.appsnado.haippNew.R.layout.role_fragment,
        container,
        false)

//        YoYo.with(Techniques.FlipInY)
//                .duration(500)
//                .repeat(0)
//                .playOn(binding!!.btnparent);
//
//        YoYo.with(Techniques.FlipInY)
//                .duration(500)
//                .repeat(0)
//                .playOn(binding!!.btnkid);

        binding?.btnparent?.setOnClickListener(this)
        binding?.btnkid?.setOnClickListener(this)





        getDeviceToken()
        calltoken()





        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(binding!!.root)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnkid -> {

                val intent =
                        Intent(mainActivity, GestureSelfUnlockActivity::class.java)
                intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, AppConstants.APP_PACKAGE_NAME)
                intent.putExtra(
                        AppConstants.LOCK_FROM,
                        AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY
                )
                startActivity(intent)
                mainActivity?.finish()
                mainActivity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                 mainActivity!!.preferenceManager!!.settype("kid")
                 mainActivity!!.setView()
                 navController!!.navigate(R.id.adddevicesFragment)
                 mainActivity!!.preferenceManager!!.setlockscreen(true)


            }
                R.id.btnparent -> {



                   callresponces()
                }




        }
    }

    private fun getDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(mainActivity!!) { instanceIdResult ->
            token = instanceIdResult.getToken()
            tokenfirebasedata  = instanceIdResult.getToken()
        }
    }
    private fun calltoken() {

        UIHelper.showDilog(mainActivity)
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        // Log.w(TAG, "getInstanceId failed", task.getException());
                        return@OnCompleteListener
                    }
                    UIHelper.hidedailog()
                    // Get new Instance ID token
                    token = task.result!!.token
                    tokenfirebasedata  = task.result!!.token
                    //  Log.i(TAG,  token);
                    // Log and toast
                })


    }


    private fun callresponces() {




        roleviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        roleviewModel!!.userLiveData!!.observe(mainActivity!!,  ProfileObserver())
        roleviewModel!!.loadDataNetwork(userid,token)
        cal_services = true


    }

    override fun createViewModel(): RoleViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        roleviewModel = ViewModelProviders.of(this, factory).get(RoleViewModel::class.java)
        return roleviewModel!!
    }


    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility =View.GONE
    }


    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()
        }
    }

    private inner class ProfileObserver : Observer<UserWrapper?> {
        override fun onChanged(reponces: UserWrapper?) {
            if(!cal_services) return
            if (reponces == null) return
                cal_services = false
                mainActivity!!.preferenceManager!!.settype("parent")
                mainActivity!!.setView()


                mainActivity!!.preferenceManager!!.setlockscreen(true)
                mainActivity!!.preferenceManager!!.settype("parent")

                navController.popBackStack(R.id.roleFragment, true);
                //MainActivity.navController!!.navigate(R.id.loginFragment)
                navController!!.navigate(R.id.homeFragment)

        }
    }
}