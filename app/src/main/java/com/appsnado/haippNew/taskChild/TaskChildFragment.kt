package com.appsnado.haippNew.taskChild

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.TaskChild
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.taskParent.AddtaskModel

class TaskChildFragment : BaseFragment<TaskViewModel>(), View.OnClickListener {
    var binding: TaskChild? = null
    var taskAdapter: TaskAdapter? = null
    var taskArrayList: ArrayList<String>? = null
    var add_services : Boolean =false
    var cal_services : Boolean =false
    private var arrayList: ArrayList<AddtaskModel>? = null

    companion object {
        @kotlin.jvm.JvmField
        var not_detail: TaskChildFragment? = null
        fun newInstance() = TaskChildFragment()
    }

    private lateinit var viewModelfeature: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.fragment_child_task,
            container,
            false
        )
        startTopToBottomAnimation()
        binding!!.claimPoint.setOnClickListener(this)
        setData()



        not_detail = this
        return binding!!.root;

    }

    private fun startTopToBottomAnimation() {
    }

    fun setData() {

        taskArrayList = ArrayList()
//
//        taskArrayList!!.add("Task")
//        taskArrayList!!.add("Task")
//        taskArrayList!!.add("Task")
//        taskArrayList!!.add("Task")
//
//        binding!!.rvTask.layoutManager = LinearLayoutManager(
//            mainActivity,
//            LinearLayoutManager.VERTICAL,
//            false
//        )
//        taskAdapter =
//            TaskAdapter(mainActivity!!, taskArrayList)
//        binding!!.rvTask.adapter = taskAdapter

        viewModelfeature!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        viewModelfeature!!.userLiveData!!.observe(mainActivity!!, ReObserver())
        viewModelfeature!!.loadDataNetwork(mainActivity?.preferenceManager?.getdevicesid()!!)
        cal_services = true



    }

    override fun createViewModel(): TaskViewModel? {
        val factory = Datamanager.getInstance()?.let {   MainViewModelFactory(it, activity) }
        viewModelfeature = ViewModelProviders.of(this, factory).get(TaskViewModel::class.java)
        return viewModelfeature
    }

    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(baseActivity!!, "Task")
    }

    override fun onClick(v: View?) {

        when(v!!.id){
            R.id.claimPoint ->
                navController.navigate(R.id.claimFragment)

        }

    }

    fun call() {
        taskArrayList!!.add("hello")
        taskAdapter!!.notifyDataSetChanged()

    }

    fun deletetask(toString: String) {
        viewModelfeature!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        viewModelfeature!!.userlivedelete!!.observe(mainActivity!!, Delete())
        viewModelfeature!!.delete(toString)
        cal_services = true

    }


    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()
        }
    }



    private inner class Delete : Observer<WebResponse<Any?>?> {
        override fun onChanged(reponces: WebResponse<Any?>?) {
            if (reponces == null) return
               mainActivity?.toast(reponces.message!!,1)
                setData()

        }
    }


    private inner class ReObserver : Observer<java.util.ArrayList<AddtaskModel>> {
        override fun onChanged(reponces: java.util.ArrayList<AddtaskModel>?) {
            if(!cal_services) return
            if (reponces == null) return
            cal_services = false
            arrayList = ArrayList()
            binding!!.rvTask.setLayoutManager(
                    LinearLayoutManager(
                            mainActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                    )
            )


            for(element in reponces!!){
                if(element.type == "wt_") {
                    if (element.claim_reward == null)
                          arrayList!!.add(element)

                }else{
                    arrayList!!.add(element)
                }
            }
            taskAdapter = TaskAdapter(mainActivity!!, arrayList)
            binding!!.rvTask.adapter = taskAdapter
            taskAdapter!!.notifyDataSetChanged()

        }
    }
}