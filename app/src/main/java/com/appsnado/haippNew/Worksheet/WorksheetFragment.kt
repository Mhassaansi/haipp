package com.appsnado.haippNew.Smartschedule

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
import com.appsnado.haippNew.Worksheet.GradeModel
import com.appsnado.haippNew.Worksheet.SubjectModel
import com.appsnado.haippNew.Worksheet.WorksheetModel
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.data.SharedPreferenceManager
import com.appsnado.haippNew.databinding.Worksheetbind

class WorksheetFragment : BaseFragment<WorksheetViewModel>(), View.OnClickListener {
    var binding: Worksheetbind? = null
    var taskAdapter: WorksheetAdapter? = null
    var Gradearray: ArrayList<GradeModel>? = null
    var Dialog: SetWorksheetDialog? = null

    var cal_services : Boolean =false
    var add_services : Boolean =false

    var cal_grade : Boolean =false
    var cal_subject : Boolean =false

    @JvmField
    var preferenceManager: SharedPreferenceManager? = null
    companion object {
        @kotlin.jvm.JvmField
        var not_detail: WorksheetFragment? = null
        fun newInstance() = WorksheetFragment()
        var Subjectarray: ArrayList<SubjectModel>? = null

        var subject_id: String? = ""
    }

    private lateinit var smartschviewModel: WorksheetViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.work_fragment,
            container,
            false
        )

        startTopToBottomAnimation()
        setData()
        preferenceManager = SharedPreferenceManager(mainActivity)
        if(preferenceManager!!.gettype() != "parent") {
            binding?.btnAdd?.visibility = View.GONE
        }



        not_detail = this
        return binding!!.root;

    }

    private fun startTopToBottomAnimation() {
    }

    fun setData() {
        binding!!.btnAdd.setOnClickListener(this)


        smartschviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        smartschviewModel!!.userGrade!!.observe(mainActivity!!, Grade())
        smartschviewModel!!.loadGrades()
        cal_grade = true

        smartschviewModel!!.userLiveData!!.observe(mainActivity!!, ReObserver())
        smartschviewModel!!.loadDataNetwork(mainActivity!!.preferenceManager?.getdevicesid())
        cal_services = true

//        taskArrayList = ArrayList()
//
//        taskArrayList!!.add("Activity Report")
//
//        binding!!.rvTask.layoutManager = LinearLayoutManager(
//            mainActivity,
//            LinearLayoutManager.VERTICAL,
//            false
//        )
//        taskAdapter =
//                WorksheetAdapter(mainActivity!!, taskArrayList)
//        binding!!.rvTask.adapter = taskAdapter




    }

    override fun createViewModel(): WorksheetViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        smartschviewModel = ViewModelProviders.of(this, factory).get(WorksheetViewModel::class.java)
        return smartschviewModel
    }

    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(baseActivity!!, "Worksheet")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> {

                Dialog = SetWorksheetDialog(mainActivity!!, "Email",Gradearray)
                mainActivity!!.supportFragmentManager?.let {
                    Dialog!!.show(
                        it,
                        null
                    )
                }

//                taskArrayList!!.add("hello")
//                taskAdapter!!.notifyDataSetChanged()
            }


        }
    }

    fun call() {
        //taskArrayList!!.add("hello")
       /// taskAdapter!!.notifyDataSetChanged()


        navController.navigate(R.id.listworkFragment)

    }

    fun callsubject(gradeId: String?) {

                smartschviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
                smartschviewModel!!.userSubject!!.observe(mainActivity!!, Subject())
                smartschviewModel!!.loadSubject(gradeId!!)
                cal_subject = true
    }

    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()
        }
    }


    private inner class Grade : Observer<java.util.ArrayList<GradeModel>> {
        override fun onChanged(reponces: java.util.ArrayList<GradeModel>?) {
            if(!cal_grade) return
            if (reponces == null) return
                cal_grade = false
                Gradearray = ArrayList()
                Gradearray = reponces



        }
    }



    private inner class ReObserver : Observer<java.util.ArrayList<WorksheetModel>> {
        override fun onChanged(reponces: java.util.ArrayList<WorksheetModel>?) {
            if(!cal_services) return
            if (reponces == null) return
            cal_services = false

                    binding!!.rvTask.layoutManager = LinearLayoutManager(
            mainActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        taskAdapter =
                WorksheetAdapter(mainActivity!!, reponces)
        binding!!.rvTask.adapter = taskAdapter
        }
    }

    private inner class Subject : Observer<java.util.ArrayList<SubjectModel>> {
        override fun onChanged(reponces: java.util.ArrayList<SubjectModel>?) {
            if(!cal_subject) return
            if (reponces == null) return
            cal_subject = false
            Subjectarray = ArrayList()
            Subjectarray = reponces

            if(Dialog != null){
                Dialog!!.calldata(Subjectarray!!)
            }


        }
    }
}