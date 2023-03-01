package com.appsnado.haippNew.Worksheet

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.appsnado.haippNew.R
import com.appsnado.haippNew.databinding.AsignworkDialogs
import java.text.SimpleDateFormat
import java.util.*


class AssignWorkSheetDailog( var activityContext: Activity,
var checkFrag: String,
) : DialogFragment(), View.OnClickListener {
    var simpleDateOnlyFormat: SimpleDateFormat? = null

    var binding: AsignworkDialogs? = null
    var spinnerCheck = 0
    var selectAdapter: ArrayAdapter<*>? = null

    var  day:String? = null
    var  stime:String? = null
    var  etime:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.set_smart_sch_dialog_asignworksheet, container, false)
        setListener()
        simpleDateOnlyFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        setData()
        return binding!!.root
    }

    private fun setData() {

        binding?.stime?.setOnClickListener {

            val c = Calendar.getInstance()
            val mYear = c[Calendar.YEAR]
            val mMonth = c[Calendar.MONTH]
            val mDay = c[Calendar.DAY_OF_MONTH]

//            c.set(Calendar.HOUR_OF_DAY,hour);
//            c.set(Calendar.MINUTE,min);
//            c.set(Calendar.SECOND, sec);
//
           // val DateFormat: String = simpleDateOnlyFormat!!.format(c.getTime())

            val datePickerDialog = DatePickerDialog(activityContext,
                    OnDateSetListener {


                        view, year, monthOfYear, dayOfMonth ->

                        val calendar = Calendar.getInstance()
                        calendar.set(year, monthOfYear, dayOfMonth)

                        val DateFormat: String = simpleDateOnlyFormat!!.format(calendar.time)

                        binding?.stime!!.setText(DateFormat)



                    }, mYear, mMonth, mDay)
            datePickerDialog.show()



        }

        binding?.etime?.setOnClickListener {
            val c = Calendar.getInstance()
            val mYear = c[Calendar.YEAR]
            val mMonth = c[Calendar.MONTH]
            val mDay = c[Calendar.DAY_OF_MONTH]

            val datePickerDialog = DatePickerDialog(activityContext,
                    OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                        val calendar = Calendar.getInstance()
                        calendar.set(year, monthOfYear, dayOfMonth)


                        val DateFormat: String = simpleDateOnlyFormat!!.format(calendar.getTime())
                        binding?.etime!!.setText(DateFormat)



                    }, mYear, mMonth, mDay)
            datePickerDialog.show()
        }

        //binding!!.tvAcceptt.isEnabled = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme)
        isCancelable = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }



    fun setListener() {
        //binding!!.tvReject.setOnClickListener(this)
        binding!!.tvAccept.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvAccept -> {

                if(WorkSheetChildFragment.not_detail != null){
                    WorkSheetChildFragment.not_detail!!.asign(binding!!.name.text.toString(),binding!!.point.text.toString(), binding?.stime!!.text.toString(),binding?.etime!!.text.toString())
                }

//                etime =  binding?.etime?.text.toString()
//                stime =  binding?.stime?.text.toString()
//
//
//                if (SmartscheduleFragment.not_detail != null) {
//                    SmartscheduleFragment.not_detail!!.call(binding?.name?.text.toString(),day,etime,stime)
//                }
                dismiss()
            }
            R.id.tvReject -> {
                dismiss()
            }

        }
    }




}