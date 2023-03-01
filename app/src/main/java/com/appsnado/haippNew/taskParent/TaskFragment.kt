package com.appsnado.haippNew.taskParent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.Comman_Pacakges.data.DbContract
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.TaskParent
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.retro.WebServiceConstants.userid
import org.json.JSONException
import org.json.JSONObject

class TaskFragment : BaseFragment<TaskParentViewModel>(), View.OnClickListener {
    var binding: TaskParent? = null
    var taskAdapter: TaskParentAdapter? = null
    var taskArrayList: ArrayList<String>? = null
    var taskDialog: SetTaskDialog? = null
    var add_services : Boolean =false
    var cal_services : Boolean =false



    companion object {
        @kotlin.jvm.JvmField
        var not_detail: TaskFragment? = null
        fun newInstance() = TaskFragment()
    }

    private lateinit var viewModeltask: TaskParentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.fragment_task_parent,
            container,
            false
        )
        startTopToBottomAnimation()
        setData()
        not_detail = this
        return binding!!.root;

    }

    private fun startTopToBottomAnimation() {
    }

    fun setData() {
        binding!!.btnAdd.setOnClickListener(this)

     /// taskArrayList = ArrayList()

       // taskArrayList!!.add("Task")



        viewModeltask!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        viewModeltask!!.userLiveData!!.observe(mainActivity!!, ReObserver())
        viewModeltask!!.loadDataNetwork(mainActivity?.preferenceManager?.getdevicesid()!!)
        cal_services = true
    }

    override fun createViewModel(): TaskParentViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        viewModeltask = ViewModelProviders.of(this, factory).get(TaskParentViewModel::class.java)
        return viewModeltask
    }

    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(baseActivity!!, "Task")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> {
                taskDialog = SetTaskDialog(mainActivity!!, "Email")
                mainActivity!!.supportFragmentManager?.let {
                    taskDialog!!.show(it, null)
                }
            }
        }
    }

    fun call(des: String, freq: String, sdate: String, edate: String, point: String, studentsObj: JSONObject) {
      //  taskArrayList!!.add("hello")
      //   taskAdapter!!.notifyDataSetChanged()

        viewModeltask!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        viewModeltask!!.ReuserLiveData!!.observe(mainActivity!!, AddObserver())
        viewModeltask!!.addtask(mainActivity!!.preferenceManager?.getdevicesid(), des,freq,sdate,edate,point,studentsObj)
        add_services = true

        NotificationSend("true","task","")

    }



    fun NotificationSend(ischeck: String, appnname: String,json: String) {
        var texts = ""
        val notification = JSONObject()
        val dataBody = JSONObject()
        val notificationBody = JSONObject()
        try {

            if(DbContract.childtoken != "") {
                texts = "You have a new task"

                dataBody.put(appnname, json)
                dataBody.put("ischeck", ischeck)
                dataBody.put("bodymsg", texts)
                notificationBody.put("title", "Haipp")
                notificationBody.put("body", "Haipp")
                notificationBody.put("click_action", "Haipp")
                notificationBody.put("notification_type", "Haipp")
                notification.put("to", DbContract.childtoken)
                notification.put("priority", "high")
                notification.put("data", dataBody)
                notification.put("notification", notificationBody)
                mainActivity?.sendNotification(notification)
                mainActivity?.sendapi(mainActivity!!.preferenceManager!!.getdevicesid(),"Task",texts,"device")
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


    private inner class ReObserver : Observer<java.util.ArrayList<AddtaskModelParent>> {
        override fun onChanged(reponces: java.util.ArrayList<AddtaskModelParent>?) {
            if(!cal_services) return
            if (reponces == null) return
            cal_services = false

            binding!!.rvTask.setLayoutManager(
                    LinearLayoutManager(
                            mainActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                    )
            )
            taskAdapter = TaskParentAdapter(mainActivity!!, reponces)
            binding!!.rvTask.adapter = taskAdapter
            taskAdapter!!.notifyDataSetChanged()

        }
    }
//    {
//        "status": 1,
//        "message": "Task and task submitions found",
//        "data": {
//        "task_id": 2,
//        "task_user_id": 2,
//        "task_device_id": 1,
//        "task_description": "fjsfjsbfs fhsjfv dfasvf asfgjf asfjaf jhfbv sfghavfe fhf sfjfvds fsfsjvfd ffvds  fgjhfsd gaeyfbsa a efgejfea aehfajwfebfhvjhvjeea ehbfajfbaefefjeafveahaefafefvf",
//        "task_frequency": "high",
//        "task_start_date": "2021-07-17",
//        "task_end_date": "2021-07-19",
//        "task_reward": "10",
//        "task_is_blocked": "0",
//        "created_at": "2021-07-16T11:12:49.000000Z",
//        "updated_at": "2021-07-16T11:12:49.000000Z",
//        "task_submition": []
//    }
//    }

    private inner class AddObserver : Observer<WebResponse<Any?>?> {
        override fun onChanged(reponces: WebResponse<Any?>?) {
            if(!add_services) return
            if (reponces == null) return
            add_services = false
            mainActivity?.toast(reponces.message!!,1)
            setData()

        }
    }
}