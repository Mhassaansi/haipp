package com.appsnado.haippNew.Smartschedule

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.MainActivity
import com.appsnado.haippNew.R
import com.appsnado.haippNew.data.SharedPreferenceManager

class SmartSchAdapter(var mainact: Activity,
                      var taskArrayList: ArrayList<SmartschedulModel>?
) :
    RecyclerView.Adapter<SmartSchAdapter.ViewHolder>() {
    var preferenceManager = SharedPreferenceManager(mainact)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainact).inflate(R.layout.item_smart_sch, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return taskArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var geo  = taskArrayList!!.get(position)

        holder.des.text = geo.ss_title
        holder.time.text = geo.ss_start_time + " - " + geo.ss_end_time
        holder.days.text = geo.ss_day


        holder.deletimg.setOnClickListener { view: View ->

            if(SmartscheduleFragment.not_detail != null){
               SmartscheduleFragment.not_detail!!.deletdata(geo.ss_id)
            }

        }
        holder.layout.setOnClickListener {

            if(preferenceManager!!.gettype() != "parent") {
                navController!!.navigate(R.id.workSheetChildFragment)
            }


        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layout = view.findViewById<LinearLayoutCompat>(R.id.contMain)
        var des = view.findViewById<TextView>(R.id.des)
        var time = view.findViewById<TextView>(R.id.time)
        var days = view.findViewById<TextView>(R.id.day)
        var deletimg = view.findViewById<ImageView>(R.id.delet)
    }

}