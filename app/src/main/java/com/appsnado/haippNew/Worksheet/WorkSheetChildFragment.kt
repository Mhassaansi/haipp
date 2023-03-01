package com.appsnado.haippNew.Worksheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.Comman_Pacakges.data.DbContract
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.MainActivity
import com.appsnado.haippNew.R
import com.appsnado.haippNew.Smartschedule.WorksheetFragment
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.WorksheetChild
import com.appsnado.haippNew.retro.WebResponse
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class WorkSheetChildFragment : BaseFragment<WorkSheetChildViewModel>(), View.OnClickListener {
    var binding: WorksheetChild? = null
    var chatMessageAdapter: WorkSheetChildAdapter? = null
    var chatArrayList: ArrayList<String>? = null
    var cal_services : Boolean =false
    var add_services : Boolean =false
    var Dialog: AssignWorkSheetDailog? = null

    var deviceid: String? = ""
    var jsonobj :JSONObject? = null

    var selectedanswer: String? = ""
    var correctannswer: String? = ""
    var geo_id: String? = ""
    var parenttoken: String? = ""



    companion object {
        @kotlin.jvm.JvmField
        var not_detail: WorkSheetChildFragment? = null
        val option = JSONArray()
        var obj = JSONObject()

        fun newInstance() = WorkSheetChildFragment()

    }

    private lateinit var viewModelfeature: WorkSheetChildViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fargment_worksheet_child,
            container,
            false
        )
        startTopToBottomAnimation()
        setData()
        geo_id = arguments?.getString("task_id")





        binding!!.assign.setOnClickListener {
            Dialog = AssignWorkSheetDailog(mainActivity!!, "Email")
            mainActivity!!.supportFragmentManager?.let {
                Dialog!!.show(
                    it,
                    null
                )
            }

        }

        if(mainActivity?.preferenceManager!!.gettype() != "parent") {
            parenttoken  = arguments?.getString("parenttoken")!!
            binding!!.assign.visibility = View.GONE
            binding!!.submit.visibility = View.VISIBLE
        }else {
            parenttoken  = DbContract.childtoken
            binding!!.assign.visibility = View.VISIBLE
            binding!!.submit.visibility = View.GONE
        }


        binding!!.submit.setOnClickListener {

           // mainActivity?.toast("Server Error",0)

            if(selectedanswer != null){
                if(selectedanswer.equals(correctannswer)){

                    val json = "{\"id\":\"1.0\",\"question_option\":\"4\"}"
                    val obj = JSONObject(json)
                    viewModelfeature.loadingStatus.observe(mainActivity!!, LoadingObserver())
                    viewModelfeature.submitLiveData.observe(mainActivity!!, SubmitObserver())
                    viewModelfeature.asssubmit(deviceid,geo_id,obj)
                    add_services = true

                }else{
                    mainActivity!!.toast("Wrong Answer",0)
                }
            }

        }

        not_detail = this
        return binding!!.root;

    }

    private fun startTopToBottomAnimation() {
    }

    fun setData() {


        viewModelfeature!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        viewModelfeature!!.userLiveData!!.observe(mainActivity!!, ReObserver())
        viewModelfeature!!.loadDataNetwork(WorksheetFragment.subject_id!!,"full_list")
        cal_services = true

//        chatArrayList = ArrayList()
//
//        chatArrayList!!.add("Task")
//        chatArrayList!!.add("Task")
//        chatArrayList!!.add("Task")
//        chatArrayList!!.add("Task")
//        chatArrayList!!.add("Task")
//        chatArrayList!!.add("Task")

//        binding!!.rvMessage.layoutManager = LinearLayoutManager(
//            mainActivity,
//            LinearLayoutManager.VERTICAL,
//            false
//        )
//        chatMessageAdapter =
//            WorkSheetChildAdapter(mainActivity!!, chatArrayList)
//        binding!!.rvMessage.adapter = chatMessageAdapter
    }

    override fun createViewModel(): WorkSheetChildViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        viewModelfeature = ViewModelProviders.of(this, factory).get(WorkSheetChildViewModel::class.java)
        return viewModelfeature
    }

    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.text("Worksheet")
    }

    override fun onClick(v: View?) {

    }

    fun asign(name: String, point: String, stime: String, etime: String) {
        viewModelfeature!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        viewModelfeature!!.ReuserLiveData!!.observe(mainActivity!!, AddObserver())
        viewModelfeature!!.addeworksheet(mainActivity!!.preferenceManager?.getUser()?.userID,mainActivity!!.preferenceManager?.getdevicesid(),WorksheetFragment.subject_id!!,name,point,stime,etime)
        add_services = true

    }

    fun addjson(
        getdevicesid: String?,
        subjectId: String?,
        obj: JSONObject,
        toString: String,
        answer: String
    ) {
          deviceid = getdevicesid
          jsonobj = obj
          selectedanswer = toString
           correctannswer = answer
    }

    private inner class AddObserver : Observer<WebResponse<Any?>?> {
        override fun onChanged(reponces: WebResponse<Any?>?) {
            if(!add_services) return
            if (reponces == null) return
            add_services = false
            mainActivity?.toast(reponces.message!!,1)
            ///setData()  worksheetba
            NotificationSend("true","Worksheet","","You have a new Work Sheet",parenttoken)


            navController.popBackStack()
        }
    }

    private inner class SubmitObserver : Observer<WebResponse<Any?>?> {
        override fun onChanged(reponces: WebResponse<Any?>?) {
            if(!add_services) return
            if (reponces == null) return
            add_services = false
            mainActivity?.toast(reponces.message!!,1)
            ///setData()  worksheetba
            NotificationSendchild("true","Worksheet","",  mainActivity!!.preferenceManager?.getdevicestitle()+" has completed the worksheet",parenttoken)


            navController.popBackStack()
        }
    }
    fun NotificationSend(
        ischeck: String,
        appnname: String,
        json: String,
        s: String,
        parenttoken: String?
    ) {

        val notification = JSONObject()
        val dataBody = JSONObject()
        val notificationBody = JSONObject()
        try {

            if(parenttoken != "") {

                dataBody.put(appnname, json)
                dataBody.put("ischeck", ischeck)
                dataBody.put("bodymsg", s)
                notificationBody.put("title", "Haipp")
                notificationBody.put("body", "Haipp")
                notificationBody.put("click_action", "Haipp")
                notificationBody.put("notification_type", "Haipp")
                notification.put("to", parenttoken)
                notification.put("priority", "high")
                notification.put("data", dataBody)
                notification.put("notification", notificationBody)
                mainActivity?.sendNotification(notification)
             //   texts = mainActivity!!.preferenceManager?.getdevicestitle()+" has completed the task"
                mainActivity?.sendapi(mainActivity!!.preferenceManager!!.getdevicesid(),appnname,s,"device")

            }else{
                Toast.makeText(mainActivity,"Please again select devices", Toast.LENGTH_LONG).show()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }




    fun NotificationSendchild(
        ischeck: String,
        appnname: String,
        json: String,
        s: String,
        parenttoken: String?
    ) {

        val notification = JSONObject()
        val dataBody = JSONObject()
        val notificationBody = JSONObject()
        try {

            if(parenttoken != "") {

                dataBody.put(appnname, json)
                dataBody.put("ischeck", ischeck)
                dataBody.put("bodymsg", s)
                notificationBody.put("title", "Haipp")
                notificationBody.put("body", "Haipp")
                notificationBody.put("click_action", "Haipp")
                notificationBody.put("notification_type", "Haipp")
                notification.put("to", parenttoken)
                notification.put("priority", "high")
                notification.put("data", dataBody)
                notification.put("notification", notificationBody)
                mainActivity?.sendNotification(notification)
                //   texts = mainActivity!!.preferenceManager?.getdevicestitle()+" has completed the task"
                mainActivity?.sendapi(mainActivity!!.preferenceManager!!.getUser()!!.userID,appnname,s,"user")

            }else{
                Toast.makeText(mainActivity,"Please again select devices", Toast.LENGTH_LONG).show()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()

        }
    }


    private inner class ReObserver : Observer<java.util.ArrayList<Questionmodel>> {
        override fun onChanged(reponces: java.util.ArrayList<Questionmodel>?) {
            if(!cal_services) return
            if (reponces == null) return
            cal_services = false

            binding!!.rvMessage.layoutManager = LinearLayoutManager(
                mainActivity,
                LinearLayoutManager.VERTICAL,
                false
            )

            chatMessageAdapter = WorkSheetChildAdapter(mainActivity!!, reponces)
            binding!!.rvMessage.adapter = chatMessageAdapter

        }
    }
}