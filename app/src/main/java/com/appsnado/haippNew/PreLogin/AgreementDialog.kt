package com.appsnado.haippNew.PreLogin

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.R
import com.appsnado.haippNew.databinding.AgreementDialogBind

class AgreementDialog(
    var activityContext: Activity,
    var checkFrag: String,
    var socialProviderListener: SocialProviderListener?
) : DialogFragment(), View.OnClickListener {

    var binding: AgreementDialogBind? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_agreement_dialog, container, false)
        setListener()
        setData()
        return binding!!.root
    }

    private fun setData() {
        binding!!.btnAccept.isEnabled = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme)
        isCancelable = true
    }

    fun setListener() {
        binding!!.btnReject.setOnClickListener(this)
        binding!!.btnAccept.setOnClickListener(this)
        binding!!.cbPrivacyPolicy.setOnClickListener(this)
        binding!!.cbTermCondition.setOnClickListener(this)
        binding!!.tvTermCondition.setOnClickListener(this)
        binding!!.tvPrivacyPolicy.setOnClickListener(this)
    }

    @SuppressLint("RestrictedApi")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAccept -> {
                if (checkFrag.equals("Email")) {
      //           navController.popBackStack(R.id.pre_login_fragment, true);
                    navController!!.navigate(R.id.loginFragment)

                } else {
                    socialProviderListener!!.setSocialProvider(checkFrag)
                }
                dismiss()
            }
            R.id.btnReject -> {
                dismiss()
            }
            R.id.cbTermCondition -> {

                isCheck(binding!!.cbTermCondition.isChecked, binding!!.cbPrivacyPolicy.isChecked)
            }
            R.id.cbPrivacyPolicy -> {

                isCheck(binding!!.cbTermCondition.isChecked, binding!!.cbPrivacyPolicy.isChecked)
            }
            R.id.tvPrivacyPolicy -> {
                dismiss()

               // navController.popBackStack(R.id.loginFragment, true);
               // navController.navigate(R.id.roleFragment)

                navController!!.navigate(R.id.privacyFragment)
//

            }


            R.id.tvTermCondition -> {
                dismiss()
                navController!!.navigate(R.id.termsandconditionFragment)
        //        )
            }
        }
    }

    fun isCheck(termConditionCheck: Boolean, privacyCheck: Boolean) {
        if (termConditionCheck && privacyCheck) {
            binding!!.btnAccept.isEnabled = true
            binding!!.btnAccept.alpha = .9f
        } else {
            binding!!.btnAccept.isEnabled = false
            binding!!.btnAccept.alpha = .3f
        }
    }
}