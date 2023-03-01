package com.appsnado.haippNew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.ilhasoft.support.validation.Validator
import com.appsnado.haippNew.Activities.MainActivity.Companion.tokenfirebasedata
import com.appsnado.haippNew.Comman_Pacakges.data.UserWrapper
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.LoginBind
import com.appsnado.haippNew.retro.WebServiceConstants.userid
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.thekhaeng.pushdownanim.PushDownAnim


class LoginFragment : BaseFragment<LoginViewModel>(), View.OnClickListener,
    Validator.ValidationListener {
    var binding: LoginBind? = null
    //    private val login: TransitionButton? = null
    lateinit var navController: NavController
    var validator: Validator? = null
    var cal_services : Boolean =false
    var regexValidate: String? = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[\$*.{}?\"!@#%&/,><\\':;|_~`^]).{8,20}"
    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var loginviewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.login_fragment,
            container,
            false
        )



        binding!!.btnLogin.setOnClickListener(this)
        binding!!.tvSignUp.setOnClickListener(this)
        binding!!.btnLeft.setOnClickListener(this)
        binding!!.tvForgotPassword.setOnClickListener(this)

        validator = Validator(binding)
        validator?.setValidationListener(this)



        YoYo.with(Techniques.FadeInDown)
            .duration(700)
            .repeat(0)
            .playOn(binding!!.etEmailAddress);


        YoYo.with(Techniques.FadeInDown)
            .duration(800)
            .repeat(0)
            .playOn(binding!!.etPassword);

        YoYo.with(Techniques.FadeInDown)
            .duration(900)
            .repeat(0)
            .playOn(binding!!.btnLogin);


        calltoken()
        getDeviceToken()
      ///  showcustomsnackbar(mainActivity,"already account exit in database")
        //aimation()
        return binding!!.root
    }

    private fun getDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(mainActivity!!) { instanceIdResult ->
            tokenfirebasedata = instanceIdResult.getToken()
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
                    tokenfirebasedata = task.result!!.token
                    //  Log.i(TAG,  token);
                    // Log and toast
                })


    }


    private fun aimation() {

        PushDownAnim.setPushDownAnimTo(binding!!.btnLogin).setScale(PushDownAnim.MODE_SCALE, 0.89f)
            .setDurationPush(
                PushDownAnim.DEFAULT_PUSH_DURATION
            )
            .setDurationRelease(PushDownAnim.DEFAULT_RELEASE_DURATION).setInterpolatorPush(
                PushDownAnim.DEFAULT_INTERPOLATOR
            )
            .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        navController = Navigation.findNavController(binding!!.root)
        // TODO: Use the ViewModel
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin ->{
                try {


                   ///navController!!.navigate(R.id.monitorAPP)

                    validator?.toValidate()

                }   catch (e :Exception){
                    e.stackTrace
                }
                //validator?.toValidate()

            }
            R.id.tvSignUp -> navController!!.navigate(R.id.signupFragment)
            R.id.tvForgotPassword -> navController!!.navigate(R.id.forgetFragment)
            R.id.btnLeft -> navController.popBackStack()
        }
    }

    override fun createViewModel(): LoginViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        loginviewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
        return loginviewModel!!
    }


    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.GONE
    }

    override fun onValidationError() {

    }

    override fun onValidationSuccess() {
        //if (Pattern.compile(regexValidate).matcher(binding!!.etPassword.text.toString()).find()) {




            loginviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
            loginviewModel!!.userLiveData!!.observe(mainActivity!!,  ResponcesObserver())
            loginviewModel!!.loadDataNetwork(binding!!.etEmailAddress.text.toString(), binding!!.etPassword.text.toString(), tokenfirebasedata)
            cal_services = true



//        } else {
//            Toast.makeText(
//                context,
//                "Invalid password. Password must be eight characters long and contains a upper case, a lower case, a number and a special character",
//                Toast.LENGTH_LONG
//            ).show()
//        }
    }



    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()
        }
    }

    override fun onResume() {
        super.onResume()

    }

    private inner class ResponcesObserver : Observer<UserWrapper?> {
        override fun onChanged(reponces: UserWrapper?) {
           if(!cal_services) return
            if (reponces == null) return
            cal_services =false
               // var bundle = bundleOf("userid" to reponces?.userID.toString())
                userid = reponces?.userID.toString()
                  if(reponces.user_is_verified == "0") {
                       // navController!!.navigate(R.id.verificationFragment,bundle)
                      navController!!.navigate(LoginFragmentDirections.actionLoginFragmentToVerificationFragment())
                      }else{
                      if(reponces.user_is_forgot == "1") {
                          var bundle = bundleOf("changepass" to "1")
                           navController!!.navigate(R.id.changepasswordFragment,bundle)
                       }else{
                         // navController!!.navigate(R.id.changepasswordFragment,bundle)
                          navController.popBackStack(R.id.loginFragment, true);
                          navController.navigate(R.id.roleFragment)

                         // navController!!.navigate(LoginFragmentDirections.actionLoginFragmentToRoleFragment())
                      }
                  }
        }
    }

    private fun removeObservers() {
    }
}