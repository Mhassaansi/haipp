package com.appsnado.haippNew.taskParent

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.R
import com.appsnado.haippNew.Spinneradaptercustom
import com.appsnado.haippNew.data.SharedPreferenceManager
import java.util.*

class TaskParentDetailAdapter(var mainact: Activity,
                              var taskArrayList: ArrayList<tasksubmition>?
) :
    RecyclerView.Adapter<TaskParentDetailAdapter.ViewHolder>(){
    var customAdapter:  Spinneradaptercustom? = null
    var arraySpinner = arrayOf<String?>("Approve", "Pending")
    var tskid : String? = null
    var preferenceManager: SharedPreferenceManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainact).inflate(R.layout.item_task_detail, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return taskArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var geo  = taskArrayList!!.get(position)
        preferenceManager = SharedPreferenceManager(mainact)

//        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
//        val date: Date = dateFormat.parse(geo.created_at) //You will get date object relative to server/client timezone wherever it is parsed
//
//        val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd") //If you need time just put specific format for time like 'HH:mm:ss'
//
//        val dateStr: String = formatter.format(date)

        val date: String = geo.created_at!!.substring(0, 10)

        holder.date.text = date
        holder.status.text = geo.ts_status
        customAdapter = Spinneradaptercustom(mainact, arraySpinner)
        setpostins(holder.spinner)


        holder.status!!.setOnClickListener {
            tskid = geo.ts_id
            if(preferenceManager!!.gettype() == "parent") {
                if (geo.ts_status.equals("pending")) {
                    holder.spinner.performClick()
                }
            }
         }


        holder.attach!!.setOnClickListener {
            if(preferenceManager!!.gettype() == "parent") {
                TaskDetailFragment.not_detail!!.imagedailog(geo.ts_image.toString())
            }
            //MainActivity.navController!!.navigate(R.id.worksheetFragment)

        }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var attach = view.findViewById<TextView>(R.id.attach)
        var layout = view.findViewById<LinearLayoutCompat>(R.id.contMain)
        var date = view.findViewById<TextView>(R.id.date)

        var status = view.findViewById<TextView>(R.id.status)
        var spinner = view.findViewById<com.appsnado.haippNew.custom_views.SpinnerPlus>(R.id.select)

    }



    private fun setpostins(spinnerGender: Spinner?) {
        spinnerGender!!.setAdapter(customAdapter)
        spinnerGender.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>?, view: View?, position: Int, arg3: Long) {
                // if (spinnerCheck++ > 0) {
                //  when (position) {

                when (position) {
                    0 -> {

                        if (TaskDetailFragment.not_detail != null) {
                            TaskDetailFragment.not_detail!!.changestatus("approved", tskid)
                        }

                    }


                    1 -> {

                        if (TaskDetailFragment.not_detail != null) {
                            TaskDetailFragment.not_detail!!.changestatus("pending", tskid)
                        }

                    }
                    // }
                }
            }
            override fun onNothingSelected(arg0: AdapterView<*>?) {
            }
        })
    }



}