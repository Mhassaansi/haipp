package com.appsnado.haippNew

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.Comman_Pacakges.data.UserWrapper
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.VerifyBinding
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.retro.WebServiceConstants.userid
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.poovam.pinedittextfield.PinField
import org.jetbrains.annotations.NotNull

class VerificationFragment : BaseFragment<VerificationViewModel>(),View.OnClickListener {
    var binding: VerifyBinding? = null
    var cTimer: CountDownTimer? = null
    var timer = 0
    var text :TextView? = null
    var cttext : CircularProgressBar? = null
    var id :String? =null
    var cal_services : Boolean =false
    var Recal_services : Boolean =false
    companion object {
        fun newInstance() = VerificationFragment()
    }

    private lateinit var verificationviewModel: VerificationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.verification_fragment,
            container,
            false
        )

        val view = binding!!.root
        binding!!.resend.setOnClickListener(this)
        text = view.findViewById<View?>(R.id.tvTimer) as TextView
        cttext= view.findViewById<View?>(R.id.circularProgressBar) as CircularProgressBar
        startTimer()

        binding?.etPinText?.onTextCompleteListener = (object : PinField.OnTextCompleteListener {
            override fun onTextComplete(@NotNull enteredText: String): Boolean {
                data_responces(binding!!.etPinText.text.toString())
                binding!!.etPinText.text = null
                return true // Return false to keep the keyboard open else return true to close the keyboard
            }
        })
         return  view;
    }

    private fun data_responces(code: String) {
            verificationviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
            verificationviewModel!!.userLiveData!!.observe(mainActivity!!,  SignupObserver())
            verificationviewModel!!.loadDataNetwork(code,userid)
            cal_services = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VerificationViewModel::class.java)
       // userid = arguments?.getString("userid")

        // TODO: Use the ViewModel
    }

    override fun createViewModel(): VerificationViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        verificationviewModel = ViewModelProviders.of(this, factory).get(VerificationViewModel::class.java)
         return verificationviewModel!!
    }

    fun startTimer() {
        if (cTimer != null) {
            cTimer!!.cancel()
        }
        timer = 0
        cTimer = object : CountDownTimer(60000, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onTick(millisUntilFinished: Long) {
                timer = Math.toIntExact(millisUntilFinished / 1000)
                text!!.text = "00:$timer"
                cttext!!.progress = timer.toFloat()
                text!!.isEnabled = false
                text!!.alpha = .4f
            }
            override fun onFinish() {

                YoYo.with(Techniques.FadeInDown)
                        .duration(700)
                        .repeat(0)
                        .playOn(binding!!.resend);
                  binding!!.resend.visibility =View.VISIBLE

            }
        }
        cTimer!!.start()
    }

    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()
        }
    }

    private inner class SignupObserver : Observer<UserWrapper?> {
        override fun onChanged(reponces: UserWrapper?) {
            if (reponces == null) return
            if(!cal_services) return
            cal_services = false
            userid = reponces?.userID.toString()
            navController.popBackStack(R.id.verificationFragment, true);
            navController!!.navigate(R.id.roleFragment)

        }
    }

    private inner class ReverficatonObserver : Observer<WebResponse<Any?>?> {
        override fun onChanged(reponces: WebResponse<Any?>?) {
             if (reponces == null) return
             if(!Recal_services) return
                 Recal_services = false
                 mainActivity?.toast(reponces.message!!,1)
                 binding?.resend?.visibility =View.GONE
                 startTimer()

        }
    }




    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.resend -> {

                  verificationviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
                  verificationviewModel!!.ReuserLiveData!!.observe(mainActivity!!,  ReverficatonObserver())
                  verificationviewModel.resend()
                  Recal_services = true

            }

        }
    }



}