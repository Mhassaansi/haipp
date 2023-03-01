package com.appsnado.haippNew.Smartschedule

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.appsnado.haippNew.R
import com.appsnado.haippNew.databinding.SmartScheduleDialogs
import java.text.SimpleDateFormat
import java.util.*


class SetSmartScheduleDialog(
    var activityContext: Activity,
    var checkFrag: String,
) : DialogFragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    var simpleDateOnlyFormat: SimpleDateFormat? = null

    var binding: SmartScheduleDialogs? = null
    var spinnerCheck = 0
    var selectAdapter: ArrayAdapter<*>? = null

    var  day:String? = null
    var  stime:String? = null
    var  etime:String? = null
    var arraySpinner =
        arrayOf<String?>("Select Days", "Monday", "Tuesday", "Wed", "Thursday", "Fri", "Sat", "Sun")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.set_smart_sch_dialog, container, false)
        setListener()
        simpleDateOnlyFormat = SimpleDateFormat("MMM d yyyy", Locale.getDefault())
        setData()
        return binding!!.root
    }


    private fun dailogbox(texts: TextView?) {
        val d: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.number_picker_dialog, null)
        d.setTitle("Set Hour")
        d.setView(dialogView)
        val numberPicker = dialogView.findViewById<View>(R.id.dialog_number_picker) as NumberPicker
        numberPicker.maxValue = 24
        numberPicker.minValue = 1
        numberPicker.wrapSelectorWheel = false

        numberPicker.setOnValueChangedListener {
            numberPicker, i, i1 -> Log.d("TAG", "onValueChange: ")
        }
        d.setPositiveButton("Done", DialogInterface.OnClickListener { dialogInterface, i ->
            Log.d("TAG", "onClick: " + numberPicker.value)

            simpleDateOnlyFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            //   val sm = SimpleDateFormat("mm-dd-yyyy")

              if(numberPicker.value.toString().length == 1){
                  var times = "0"+numberPicker.value.toString()+":00"
                   texts?.text = times
              }else{

                  var times = numberPicker.value.toString()+":00"
                  texts?.text = times

              }




        })
        d.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i -> })
        val alertDialog: android.app.AlertDialog? = d.create()
        alertDialog?.show()

    }


    private fun setData() {




        binding?.stime?.setOnClickListener {
            dailogbox(binding?.stime)
//            val mcurrentTime = Calendar.getInstance()
//            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
//            val minute = mcurrentTime[Calendar.MINUTE]
//            val mTimePicker: TimePickerDialog
//            mTimePicker = TimePickerDialog(context, OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
//                binding?.stime?.setText("$selectedHour:$selectedMinute")
//
//
//                var h = selectedHour.toString()
//                var m = selectedMinute.toString()
//
//                if(h.length == 1){
//                    binding?.stime?.setText("0"+"$selectedHour:$selectedMinute")
//                }
//                if(m.length == 1){
//                    binding?.stime?.setText("$selectedHour:$selectedMinute"+"0")
//                }
//
//            },
//
//                    hour, minute, true) //Yes 24 hour time
//
//            stime =  binding?.stime?.text.toString()
//            mTimePicker.setTitle("Select Time")
//            mTimePicker.show()

        }






        binding?.etime?.setOnClickListener {
            dailogbox(binding?.etime)

//            val mcurrentTime = Calendar.getInstance()
//            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
//            val minute = mcurrentTime[Calendar.MINUTE]
//            val mTimePicker: TimePickerDialog
//            mTimePicker = TimePickerDialog(context, OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
//                binding?.etime?.setText("$selectedHour:$selectedMinute")
//
//                var h = selectedHour.toString()
//                var m = selectedMinute.toString()
//
//                if(h.length == 1){
//                    binding?.etime?.setText("0"+"$selectedHour:$selectedMinute")
//                }
//                if(m.length == 1){
//                    binding?.etime?.setText("$selectedHour:$selectedMinute"+"0")
//                }
//
//            }, hour, minute, true) //Yes 24 hour time
//
//
//
//            mTimePicker.setTitle("Select Time")
//            mTimePicker.show()



//            SingleDateAndTimePickerDialog.Builder(context)
//                    .backgroundColor(resources.getColor(R.color.colortitlebarbg))
//                    .mainColor(Color.GREEN)
//                    .bottomSheet()
//                    .curved()
//                    .displayMinutes(true)
//                    .displayHours(true)
//                    .displayDays(true)
//                    .displayMonth(false)
//                    .displayYears(false)
//                    .displayDaysOfMonth(false)
//                    .titleTextColor(resources.getColor(R.color.white))
//                    .listener(SingleDateAndTimePickerDialog.Listener {
//
//                        date ->  stime
//                       var  etime = simpleDateOnlyFormat?.format(date)
//                        binding?.etime?.text = etime.toString()
//
//                    })
//                    .display()
        }

        //binding!!.tvAcceptt.isEnabled = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme)
        isCancelable = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
    }

    private fun setupSpinner() {
        binding!!.days.setOnClickListener(this)
        binding!!.select.onItemSelectedListener = this
        selectAdapter =
            ArrayAdapter<Any?>(
                activityContext!!,
                android.R.layout.simple_spinner_item,
                arraySpinner
            )
        selectAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.select.adapter = selectAdapter
    }


    fun setListener() {
        binding!!.tvReject.setOnClickListener(this)
        binding!!.tvAccept.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvAccept -> {

//                if(TaskFragment.not_detail != null) {
//                    TaskFragment.not_detail!!.call(binding?.des?.text.toString(),frequecy!!,"2021-01-01","2021-12-30",binding?.point?.text.toString(),studentsObj)
//                }



                etime =  binding?.etime?.text.toString()
                stime =  binding?.stime?.text.toString()



                if(binding?.stime?.text.toString() == "24:00"){
                    stime = "23:59"
                }






                if (SmartscheduleFragment.not_detail != null) {
                    SmartscheduleFragment.not_detail!!.call(binding?.name?.text.toString(),day,binding?.etime?.text.toString(),stime)
                }
                dismiss()
            }
            R.id.tvReject -> {
                dismiss()
            }
            R.id.days -> binding!!.select.performClick()

//            R.id.tvTermCondition -> {
//                dismiss()
//
//                    true,
//                    true
//                )
//            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (spinnerCheck++ > 0) {
            binding!!.days.text = arraySpinner[position]
            day = arraySpinner[position]
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


}