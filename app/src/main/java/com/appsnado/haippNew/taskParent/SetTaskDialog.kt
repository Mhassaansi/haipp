package com.appsnado.haippNew.taskParent

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.R
import com.appsnado.haippNew.databinding.SetTaskDialogbind
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SetTaskDialog(
    var activityContext: Activity,
    var checkFrag: String,
    ) : DialogFragment(), View.OnClickListener,AdapterView.OnItemSelectedListener{

    var binding: SetTaskDialogbind? = null
    var spinnerCheck = 0
    var selectAdapter: ArrayAdapter<*>? = null
    var taskAdapter :Dateadapter? =null
    var taskArrayList: ArrayList<String>? = null
    var taskArrayListweek: ArrayList<String>? = null
    val studentsObj  = JSONObject()

    var listss = mutableListOf(1, 2, 3)
    var weekis =
        arrayOf<String?>()

    var frequecy: String? = null
    var textviewstring: String? = null
    var arraySpinner =
            arrayOf<String?>("Once", "Daily", "Weekly", "Monthly")

    @SuppressLint("WrongViewCast")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.set_task_dialog, container, false)

        taskArrayList = ArrayList()
        setListener()
        setData()
        taskArrayListweek = ArrayList()

        binding!!.weekdayslist.setOnWeekdaysChangeListener { view, clickedDayOfWeek, selectedDays ->

            //taskArrayListweek = selectedDays
           // taskArrayListweek!!.add(clickedDayOfWeek.toString())
            listss = selectedDays
            // Do Something
        }

        return binding!!.root;
    }

    private fun setData() {
        //binding!!.tvAcceptt.isEnabled = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setStyle(STYLE_NO_FRAME, R.style.DialogTheme)
        isCancelable = true
    }

    fun setListener() {
        binding!!.tvReject.setOnClickListener(this)
        binding!!.tvAcceptt.setOnClickListener(this)

        binding!!.adddlist.setOnClickListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
    }

    private fun setupSpinner() {
        binding!!.frequency.setOnClickListener(this)
        binding!!.select.onItemSelectedListener = this
        selectAdapter =
            ArrayAdapter<Any?>(
                activityContext!!,
                android.R.layout.simple_spinner_item,
                arraySpinner
            )
       selectAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_item)

       // selectAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.select.adapter = selectAdapter





    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvAcceptt -> {

                timearray()
                dismiss()


            }
            R.id.adddlist -> {
               timedate()
            }
            R.id.tvReject -> {
                dismiss()
            }
            R.id.frequency -> binding!!.select.performClick()
//            R.id.tvTermCondition -> {
//                dismiss()
//
//                    true,
//                    true
//                )
//            }
        }
    }

    private fun timearray() {
       // Weekly
        val jsonArraymonth = JSONArray()
        val jsonArrayWeekly = JSONArray()
        val jsonArrayOnce = JSONArray()
        val jsonArrayDaily = JSONArray()

        if(frequecy.equals("Monthly")) {
            for (info in taskArrayList!!) {
                val obj = JSONObject()

                try {

                    obj.put("date", info)
                } catch (e: JSONException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
                jsonArraymonth.put(obj)

            }

        }else if(frequecy.equals("Weekly")){
            for (info in listss!!) {
                val obj = JSONObject()

                try {

                    obj.put("date", info)
                } catch (e: JSONException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
                jsonArrayWeekly.put(obj)

            }
        }else if(frequecy.equals("Once")){
                val obj = JSONObject()
             try {

                    obj.put("date", "Once")
                } catch (e: JSONException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            jsonArrayOnce.put(obj)


        }else if(frequecy.equals("Daily")){
            val obj = JSONObject()
            try {
                obj.put("date", "Daily")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            jsonArrayDaily.put(obj)


        }
            studentsObj.put("Monthly",jsonArraymonth)
            studentsObj.put("Weekly",jsonArrayWeekly)
            studentsObj.put("Daily",jsonArrayDaily)
            studentsObj.put("Once",jsonArrayOnce)
          Log.d("TAG", "timearray: "+studentsObj)

        if(studentsObj != null)
        if(TaskFragment.not_detail != null) {
            TaskFragment.not_detail!!.call(binding?.des?.text.toString(),frequecy!!,"2021-01-01","2021-12-30",binding?.point?.text.toString(),studentsObj)
        }

    }
   // {"Monthly":[{"date":"10\/09\/2021"},{"date":"24\/09\/2021"}],"Weekly":[{"date":2},{"date":3},{"date":4},{"date":5}]}
    private fun timedate() {
        val calendar = Calendar.getInstance()
        val dialog = DatePickerDialog(
            requireActivity(),
            OnDateSetListener { arg0, year, month, day_of_month ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month
                calendar[Calendar.DAY_OF_MONTH] = day_of_month
                val myFormat = "dd/MM/yyyy"
                val sdf =
                    SimpleDateFormat(myFormat, Locale.getDefault())
                taskArrayList!!.add(sdf.format(calendar.time))





                binding!!.last.setLayoutManager(
                    LinearLayoutManager(
                        activityContext,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                )
                taskAdapter = Dateadapter(activityContext,  taskArrayList)
                binding!!.last.adapter = taskAdapter
                taskAdapter!!.notifyDataSetChanged()
                //your_edittext.setText(sdf.format(calendar.time))
            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )


//                dialog.datePicker.minDate =
//                    calendar.timeInMillis // TODO: used to hide previous date,month and year
//
//                calendar.add(Calendar.YEAR, 0)
//                dialog.datePicker.maxDate =
//                    calendar.timeInMillis // TODO: used to hide future date,month and year

        dialog.show()

    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (spinnerCheck++ > 0) {
            binding!!.frequency.text = arraySpinner[position]
            frequecy = arraySpinner[position]
            if(position == 2){
                binding!!.weekdays.visibility = View.VISIBLE
                binding!!.clan.visibility = View.GONE



            }else if(position == 3){

                binding!!.weekdays.visibility = View.GONE
                binding!!.clan.visibility = View.VISIBLE


            }else{
                binding!!.weekdays.visibility = View.GONE
                binding!!.clan.visibility = View.GONE
            }

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}