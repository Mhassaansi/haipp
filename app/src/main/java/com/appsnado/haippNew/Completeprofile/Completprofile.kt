package com.appsnado.haippNew.Completeprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.appsnado.haippNew.Comman_Pacakges.data.UserWrapper
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.CompletBind
import com.appsnado.haippNew.databinding.RoleBind
import com.appsnado.haippNew.retro.WebServiceConstants
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class Completprofile : BaseFragment<CompleteViemodel>(), View.OnClickListener {
    lateinit var navController: NavController
    var binding: CompletBind? = null
    var cal_services : Boolean =false
    var token :String?= null

    companion object {
        fun newInstance() = Completprofile()
    }

    private lateinit var compviewModel: CompleteViemodel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                com.appsnado.haippNew.R.layout.complet_profile,
                container,
                false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(binding!!.root)
        binding?.update?.setOnClickListener(this)

        YoYo.with(Techniques.FlipInY)
                .duration(500)
                .repeat(0)
                .playOn(binding!!.update);

        getDeviceToken()
        calltoken()
    }


    private fun getDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(mainActivity!!) { instanceIdResult ->
            token = instanceIdResult.getToken()
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
                    //  Log.i(TAG,  token);
                    // Log and toast
                })


    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnkid -> {
                    mainActivity!!.preferenceManager!!.settype("kid")
                    mainActivity!!.setView()
                    navController!!.navigate(R.id.adddevicesFragment)
               }
            R.id.update -> callresponces()




        }
    }

    private fun callresponces() {

        compviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        compviewModel!!.userLiveData!!.observe(mainActivity!!,  ProfileObserver())
        compviewModel!!.loadDataNetwork(WebServiceConstants.userid,token)
        cal_services = true


    }

    override fun createViewModel(): CompleteViemodel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        compviewModel = ViewModelProviders.of(this, factory).get(CompleteViemodel::class.java)
        return compviewModel!!
    }


    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.GONE
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
            navController.popBackStack(R.id.roleFragment, true);
            navController!!.navigate(R.id.homeFragment)

        }
    }
}