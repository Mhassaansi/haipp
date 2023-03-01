package com.appsnado.haippNew.PreLogin

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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
import com.appsnado.haippNew.databinding.PreLoginBinding
import com.appsnado.haippNew.retro.WebServiceConstants
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import java.util.*

class PreLoginFragment : BaseFragment<PreLoginViewModel>() ,View.OnClickListener,SocialProviderListener{
    lateinit var navController: NavController
    var agreementDialog: AgreementDialog? = null
    var binding: PreLoginBinding? = null
    private var mIntentInProgress = true
    val GOOGLE_SIGNIN = 0
    var cal_services : Boolean = false
    private var callbackManager: CallbackManager? = null

    // var facebookLoginHelper = FacebookLoginHelper()
    companion object {
        fun newInstance() = PreLoginFragment()
    }

    private lateinit var previewModel: PreLoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,com.appsnado.haippNew.R.layout.pre_login_fragment, container, false)
        FacebookSdk.sdkInitialize(FacebookSdk.getApplicationContext())
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS)
        YoYo.with(Techniques.FadeInDown)
                .duration(700)
                .repeat(0)
                .playOn(binding!!.email);

        binding!!.email.setOnClickListener(this)
        binding!!.btnGoogle1.setOnClickListener(this)
        binding!!.btnFacebook.setOnClickListener(this)

        YoYo.with(Techniques.FadeInDown)
                .duration(800)
                .repeat(0)
                .playOn(binding!!.btnGoogle1);

        YoYo.with(Techniques.FadeInDown)
                .duration(900)
                .repeat(0)
                .playOn(binding!!.btnFacebook);

        return binding!!.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
         viewModel = ViewModelProvider(this).get(PreLoginViewModel::class.java)
         navController = Navigation.findNavController(binding!!.root)

    }


        override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.email -> {
                agreementDialog = AgreementDialog(mainActivity!!, "Email", this)
                mainActivity!!.supportFragmentManager?.let {
                    agreementDialog!!.show(
                        it,
                        null
                    )
                }
            }
                R.id.btnGoogle1 ->   {


                    socialGmail(mainActivity?.googleApiClient)
                  ///  Toast.makeText(mainActivity, "Working", Toast.LENGTH_SHORT).show()


                }///
                R.id.btnFacebook ->  {


                    sociallogin_test()
                    ///Toast.makeText(mainActivity, "Working", Toast.LENGTH_SHORT).show()

                }




                //R.id.email -> navController!!.navigate(R.id.loginFragment)
                 R.id.btnreg -> navController!!.navigate(R.id.verificationFragment)


        }
    }


    override fun createViewModel(): PreLoginViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        previewModel = ViewModelProviders.of(this, factory).get(PreLoginViewModel::class.java)
        return previewModel!!
    }

    override fun setTitleBar(titleBar: TitleBar?) {
       titleBar?.visibility = View.GONE

    }

    override fun setSocialProvider(provider: String) {
//        if (provider.equals(GOOGLE)) {
//            mGoogleSignInClient.signOut()
//            signInGoogle()
//        } else if (provider.equals(FACEBOOK)) {
//           // fbLogin()
//        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {

        Log.d("", "handleSignInResult:" + result.isSuccess)
        if (result.isSuccess) {
            val account = result.signInAccount
            val personFirstName = account!!.givenName
            val personLastName = account.familyName
            val personEmail = account.email
            val personId = account.id
            val personPhoto = account.photoUrl

             //webservices!!.getservices()!!.Social("", "")!!.enqueue(ResponcesCallback())

              // gmail(account)
            previewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
            previewModel!!.userLiveData!!.observe(mainActivity!!,  ResponcesObserver())
            previewModel!!.loadDataNetwork(personId,"google")
            cal_services = true

        }
    }


//    fun gmail(account: GoogleSignInAccount) {
//        val personFirstName = account.givenName
//        val personLastName = account.familyName
//        val personEmail = account.email
//        val personId = account.id
//        val personPhoto = account.photoUrl
//        socialSignInService( personId, personEmail, "google", profile.userName!!)
//    }
    fun socialGmail(googleApiClient: GoogleApiClient?) {

        if (mIntentInProgress) {
            val signInIntent = googleApiClient?.let { Auth.GoogleSignInApi.getSignInIntent(it) }
            startActivityForResult(signInIntent, GOOGLE_SIGNIN)
            mIntentInProgress = false

        } else {
            //  SettingsMain.hidetext2();

//            val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
//            val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(mainActivity!!, gso)
//            googleSignInClient.signOut()
//            mIntentInProgress = true

            val opr = googleApiClient?.let { Auth.GoogleSignInApi.silentSignIn(it) }
            if (opr!!.isDone) {
                Log.d("s", "Got cached sign-in")
                val result = opr.get()
                handleSignInResult(result)
            } else {
                opr.setResultCallback { googleSignInResult -> handleSignInResult(googleSignInResult) }
            }
        }
    }
    private fun sociallogin_test() {
        LoginManager.getInstance().logInWithReadPermissions(mainActivity,
            Arrays.asList("email", "public_profile"))
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.i("SignInFragment", "facebook error$loginResult")

                    previewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
                    previewModel!!.userLiveData!!.observe(mainActivity!!,  ResponcesObserver())
                    previewModel!!.loadDataNetwork(loginResult.accessToken.toString(),"facebook")
                    cal_services = true

                   // signViewModel!!.fbUserProfile(loginResult.accessToken)
                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    Log.e("SignInFragment", "facebook error" + exception.stackTrace)
                }
            })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GOOGLE_SIGNIN) {
            val result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }
            result?.let { handleSignInResult(it) }
        } else {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
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
            WebServiceConstants.userid = reponces?.userID.toString()
            if(reponces.user_is_forgot == "1") {
                    var bundle = bundleOf("changepass" to "1")
                    navController!!.navigate(R.id.changepasswordFragment,bundle)
                }else{
                    // navController!!.navigate(R.id.changepasswordFragment,bundle)
                    navController.popBackStack(R.id.pre_login_fragment, true);
                    navController.navigate(R.id.roleFragment)

                    // navController!!.navigate(LoginFragmentDirections.actionLoginFragmentToRoleFragment())
                }

        }
    }




}