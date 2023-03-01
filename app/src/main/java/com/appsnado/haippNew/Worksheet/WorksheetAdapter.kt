package com.appsnado.haippNew.Smartschedule

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.R
import com.appsnado.haippNew.Worksheet.WorksheetModel
import java.util.ArrayList

class WorksheetAdapter(var mainact: Activity,
                       var taskArrayList: ArrayList<WorksheetModel>?
) :
    RecyclerView.Adapter<WorksheetAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainact).inflate(R.layout.item_worksheet_sch, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return taskArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var geo  = taskArrayList!!.get(position)

        holder.des.text = geo.wt_description
        holder.time.text = geo.wt_start_date + "-" + geo.wt_end_date
        holder.point.text = geo.wt_reward

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var des = view.findViewById<TextView>(R.id.des)
        var time = view.findViewById<TextView>(R.id.time)
        var point = view.findViewById<TextView>(R.id.day)

    }

}