package com.appsnado.haippNew.Smartschedule

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.appsnado.haippNew.R
import com.appsnado.haippNew.Smartschedule.WorksheetFragment.Companion.subject_id
import com.appsnado.haippNew.Worksheet.GradeModel
import com.appsnado.haippNew.Worksheet.SpinneradapterGrade
import com.appsnado.haippNew.Worksheet.SpinneradapterSubject
import com.appsnado.haippNew.Worksheet.SubjectModel
import com.appsnado.haippNew.databinding.WorksheetDialog
import kotlinx.android.synthetic.main.set_work_sheet_dialog.*

class SetWorksheetDialog(
        var activityContext: Activity,
        var checkFrag: String,
        var gradearray: ArrayList<GradeModel>?,
) : DialogFragment(), View.OnClickListener {
    var customAdapter:  SpinneradapterGrade? = null
    var customAdaptersubj:  SpinneradapterSubject? = null
    var subjectarraylist: ArrayList<SubjectModel>?= null

    var binding: WorksheetDialog? = null
    var spinnerCheck = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.set_work_sheet_dialog, container, false)

//        activityContext!!.getWindow()!!.setFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND,
//                WindowManager.LayoutParams.DIM_AMOUNT_CHANGED)
//        activityContext!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
//      //  activityContext!!.window?.setLayout(600,400)

        setListener()
        setData()
        return binding!!.root
    }

    private fun setData() {

        customAdapter = SpinneradapterGrade(activityContext, gradearray)
        setpostins(binding!!.grade)

        binding!!.txtgrade.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       /// setStyle(STYLE_NO_FRAME, R.style.DialogTheme)
        isCancelable = true
    }

    private fun setpostins(spinnerGender: Spinner?) {
        spinnerGender!!.setAdapter(customAdapter)
        spinnerGender.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>?, view: View?, position: Int, arg3: Long) {
                binding!!.txtgrade.text = gradearray!!.get(position).grade_title
                if (spinnerCheck++ > 0) {
                    //when (position) {

                        if(WorksheetFragment.not_detail != null){
                            WorksheetFragment.not_detail!!.callsubject(gradearray!!.get(position).grade_id)
                        }
                   // }
                }
            }
            override fun onNothingSelected(arg0: AdapterView<*>?) {
            }
        })
    }

    private fun setsubject(spinnerGender: Spinner?) {
        spinnerGender!!.setAdapter(customAdaptersubj)
        spinnerGender.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>?, view: View?, position: Int, arg3: Long) {
                binding!!.sub.text = subjectarraylist!!.get(position).subject_title
                if (spinnerCheck++ > 0) {
                    //when (position) {
                    subject_id = subjectarraylist!!.get(position).subject_id

                    // }
                }
            }
            override fun onNothingSelected(arg0: AdapterView<*>?) {
            }
        })
    }

    fun setListener() {
       // binding!!.tvReject.setOnClickListener(this)
        binding!!.tvAccept.setOnClickListener(this)
        binding!!.sub.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvAccept -> {
                if(WorksheetFragment.not_detail != null) {
                    WorksheetFragment.not_detail!!.call()
                }
                dismiss()
            }
            R.id.tvReject -> {
                dismiss()
            }
            R.id.txtgrade -> {
                binding!!.grade.performClick()
            }

            R.id.sub -> {
                binding!!.subject.performClick()
            }
//            R.id.tvTermCondition -> {
//                dismiss()
//
//                    true,
//                    true
//                )
//            }
        }
    }

    fun calldata(subjectarray: java.util.ArrayList<SubjectModel>) {
        subjectarraylist = subjectarray
        customAdaptersubj = SpinneradapterSubject(activityContext, subjectarray)
        setsubject(binding!!.subject)
    }


}