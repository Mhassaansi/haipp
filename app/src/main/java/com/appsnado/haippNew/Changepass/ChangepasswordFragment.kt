package com.appsnado.haippNew.Changepass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.MainActivity
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.ChangepassBinding
import com.appsnado.haippNew.retro.WebResponse

class ChangepasswordFragment : BaseFragment<ChangepasswordViewModel>(),View.OnClickListener {
    var binding: ChangepassBinding? = null
    //    private val login: TransitionButton? = null
    lateinit var navController: NavController
    var changeview :String? = ""
    companion object {
        fun newInstance() = ChangepasswordFragment()
    }

    private lateinit var changepassviewModel: ChangepasswordViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,com.appsnado.haippNew.R.layout.changepassword_fragment, container, false)
        changeview = arguments?.getString("changepass")
        if(changeview == "1")
         binding?.etEmailAddress?.visibility = View.GONE


        binding?.btnLogin?.setOnClickListener(this)
         return binding!!.root
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnLogin -> {

                if(changeview == "1")
                   passwordchange("1",binding?.cPassword?.text.toString())
                else
                   passwordchange(binding?.etEmailAddress?.text.toString(),binding?.cPassword?.text.toString())
                  /// navController!!.navigate(R.id.homeFragment)


            }
            R.id.tvSignUp -> navController!!.navigate(R.id.signupFragment)
            R.id.btnLeft -> navController.popBackStack()
        }
    }

    private fun passwordchange(oldpass : String, newpass :String) {

        if(binding!!.etPassword.text.toString().equals(binding!!.cPassword.text.toString())) {
            changepassviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
            changepassviewModel!!.userLiveData!!.observe(mainActivity!!, forgetObserver())
            changepassviewModel.loadDataNetwork(oldpass, newpass);
        }else{
            mainActivity?.toast("Password doesn't match",0)
        }

    }

    override fun createViewModel(): ChangepasswordViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        changepassviewModel = ViewModelProviders.of(this, factory).get(ChangepasswordViewModel::class.java)
        return changepassviewModel!!
    }


    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.txtTitleName?.text = "Change Password"
        titleBar?.btnLeft?.visibility = View.VISIBLE
        titleBar?.setBtnLeft(R.drawable.backbutton, View.OnClickListener {
            navController!!.popBackStack()
        });
    }

    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()
        }
    }

    private inner class forgetObserver : Observer<WebResponse<Any?>?> {
        override fun onChanged(reponces: WebResponse<Any?>?) {
            if (reponces == null) return
               mainActivity?.toast(reponces!!.message!!,1)
               navController.popBackStack()
        }
    }
}