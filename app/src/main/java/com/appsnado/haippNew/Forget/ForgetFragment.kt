package com.appsnado.haippNew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.ForgetBinding
import com.appsnado.haippNew.retro.WebResponse

class ForgetFragment : BaseFragment<ForgetViewModel>(),View.OnClickListener {

    var binding: ForgetBinding? = null
    var cal_services : Boolean =false
    companion object {
        fun newInstance() = ForgetFragment()
    }

    private lateinit var frogetviewmodel: ForgetViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // return inflater.inflate(R.layout.forget_fragment, container, false)

        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.forget_fragment,
            container,
            false)


        binding!!.btnLeft.setOnClickListener(this)
        binding!!.reset.setOnClickListener(this)
        return binding?.root
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnLeft ->  navController.popBackStack()
            R.id.reset  -> {

                frogetviewmodel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
                frogetviewmodel!!.userLiveData!!.observe(mainActivity!!,  forgetObserver())
                frogetviewmodel.loadDataNetwork(binding?.etEmailAddress?.text.toString());
                cal_services = true
            }

        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun createViewModel(): ForgetViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        frogetviewmodel = ViewModelProviders.of(this, factory).get(ForgetViewModel::class.java)
        return frogetviewmodel!!
    }

    override fun setTitleBar(titleBar: TitleBar?) {

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
             if(!cal_services) return
                 cal_services = false
                 mainActivity?.toast(reponces!!.message!!,1)
                 navController.popBackStack()
        }
    }

}