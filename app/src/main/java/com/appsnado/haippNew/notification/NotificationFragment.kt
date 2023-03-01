package com.appsnado.haippNew.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.Adddevices.Adddevicesmodel
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.MainActivity
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.Notificationbind
import com.appsnado.haippNew.retro.WebResponse
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class NotificationFragment :  BaseFragment<NotificationViewModel>() {
    var binding: Notificationbind? = null
    var kidAdapter: Notificationadatpter? = null
    var kidsArrayList: ArrayList<Adddevicesmodel>? = null
    var cal_services : Boolean =false
    var add_services : Boolean =false
    private var childtokenref : DatabaseReference?=null
    var kiddevicesArrayList: ArrayList<Notificationmodel>?= null
    private var tokenlistener: ValueEventListener? =null

    companion object {
        @kotlin.jvm.JvmField
        var not_detail: NotificationFragment? = null
        fun newInstance() = NotificationFragment()
    }

    private lateinit var viewModelkids: NotificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.notification_fragment,
            container,
            false
        )
        // startTopToBottomAnimation()
        setData()
        not_detail = this
        return binding!!.root;

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
    private fun startTopToBottomAnimation() {
//        val animation = AnimationUtils.loadAnimation(mainActivity, com.appsnado.haipp.R.anim.slide_in_from_top)
//        binding?.linearLayoutCompat2!!.startAnimation(animation)

    }

    fun setData() {

        viewModelkids!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        viewModelkids!!.userLiveData!!.observe(mainActivity!!,  ReObserver())


        if(mainActivity!!.preferenceManager!!.gettype() != "parent"){
            viewModelkids!!.loadDataNetwork(mainActivity!!.preferenceManager!!.getdevicesid()!!,"device")
        }else{
            viewModelkids!!.loadDataNetwork(mainActivity!!.preferenceManager!!.getUser()!!.userID!!,"user")
        }
        cal_services = true




    }



    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()
        }
    }

    private inner class ReObserver : Observer<java.util.ArrayList<Notificationmodel>> {
        override fun onChanged(reponces: java.util.ArrayList<Notificationmodel>?) {
            if(!cal_services) return
            if (reponces == null) return
            cal_services = false
//            kiddevicesArrayList = ArrayList()
//            kiddevicesArrayList!!.addAll(reponces)

            Collections.reverse(reponces);
            binding!!.rvFeatures.setLayoutManager(
                LinearLayoutManager(
                    mainActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            )
            kidAdapter = Notificationadatpter(mainActivity!!, reponces,mainActivity?.preferenceManager!!.gettype())
            binding!!.rvFeatures.adapter = kidAdapter

        }
    }


    private inner class AddObserver : Observer<WebResponse<Any?>?> {
        override fun onChanged(reponces: WebResponse<Any?>?) {
            if(!add_services) return
            if (reponces == null) return
            add_services = false
            mainActivity?.toast(reponces.message!!,1)
            setData()

        }
    }
    override fun createViewModel(): NotificationViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        viewModelkids = ViewModelProviders.of(this, factory).get(NotificationViewModel::class.java)
        return viewModelkids!!
    }

    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.txtTitleName?.text = "Notifications"
        titleBar?.btnLeft?.visibility = View.VISIBLE
        titleBar?.setBtnLeft(R.drawable.backbutton, View.OnClickListener {
            navController!!.popBackStack()
        });

    }



}