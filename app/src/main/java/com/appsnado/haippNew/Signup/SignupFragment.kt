package com.appsnado.haippNew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.ilhasoft.support.validation.Validator
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.Signup.signupmodel
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.MainFragBinding
import com.appsnado.haippNew.databinding.SignupBinding
import com.appsnado.haippNew.retro.WebServiceConstants.userid
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import java.util.regex.Pattern

class SignupFragment : BaseFragment<SignupViewModel>(), View.OnClickListener, Validator.ValidationListener {
    var binding: SignupBinding? = null
    var validator: Validator? = null
    var signupviewModel: SignupViewModel? = null
    lateinit var navController: NavController
    var cal_services : Boolean =false
    var token :String?= null

    var regexValidate: String? =
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[\$*.{}?\"!@#%&/,><\\':;|_~`^]).{8,20}"

    companion object {
        fun newInstance() = SignupFragment()
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.signup_fragment,
            container,
            false
        )
        binding!!.btnreg.setOnClickListener(this)
        binding!!.btnLeft.setOnClickListener(this)
        binding!!.tvSignUp.setOnClickListener(this)
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
                .playOn(binding!!.etcfPassword);

        YoYo.with(Techniques.FadeInDown)
                .duration(1000)
                .repeat(0)
                .playOn(binding!!.btnreg);

        calltoken()
        getDeviceToken()






        return binding!!.root
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

    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()
        }
    }

    private inner class SignupObserver : Observer<signupmodel?> {
        override fun onChanged(reponces: signupmodel?) {
            if(!cal_services) return
            if (reponces == null) return
                   cal_services = false
                   userid = reponces?.userID.toString()
                   var bundle = bundleOf("userid" to reponces?.userID.toString())
                   navController!!.navigate(R.id.verificationFragment,bundle)

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(binding!!.root)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnreg -> validator?.toValidate()
            R.id.btnLeft -> navController.popBackStack()
            R.id.tvSignUp -> navController.navigate(R.id.loginFragment)

        }
    }

    override fun onValidationError() {

    }

    override fun onValidationSuccess() {
        if (Pattern.compile(regexValidate).matcher(binding!!.etPassword.text.toString()).find()) {
            if (Pattern.compile(regexValidate).matcher(binding!!.etcfPassword.text.toString()).find()) {
                if(binding!!.etPassword.text.toString().equals(binding!!.etcfPassword.text.toString())){

                    signupviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
                    signupviewModel!!.userLiveData!!.observe(mainActivity!!,  SignupObserver())
                    signupviewModel!!.loadDataNetwork(binding!!.etEmailAddress.text.toString(), binding!!.etPassword.text.toString())
                    cal_services = true




                }else {
//                    Toast.makeText(
//                            context,
//                            "Password doesn't match",
//                            Toast.LENGTH_LONG
//                        ).show()
                      mainActivity?.toast("Password doesn't match",0)
                    }
            } else {
                Toast.makeText(
                        context,
                        "Invalid confirm password. Password must be eight characters long and contains a upper case, a lower case, a number and a special character",
                        Toast.LENGTH_LONG
                    ).show()
            }
        } else {
            Toast.makeText(
                context,
                "Invalid password. Password must be eight characters long and contains a upper case, a lower case, a number and a special character",
                Toast.LENGTH_LONG
            ).show()
        }

       // navController!!.navigate(R.id.verificationFragment)
    }

    override fun createViewModel(): SignupViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        signupviewModel = ViewModelProviders.of(this, factory).get(SignupViewModel::class.java)
        return signupviewModel!!
    }

    override fun setTitleBar(titleBar: TitleBar?) {
    }





}