package com.appsnado.haippNew.taskChild

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.R
import com.appsnado.haippNew.Smartschedule.WorksheetFragment.Companion.subject_id
import com.appsnado.haippNew.taskParent.AddtaskModel

class TaskAdapter(var mainact: Activity,
                  var taskArrayList: ArrayList<AddtaskModel>?
) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainact).inflate(R.layout.item_parent_task, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return taskArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var geo  = taskArrayList!!.get(position)
        holder.des.text = geo.description
        if(geo.type!!.equals("task_")){

            holder.freq.text = geo.frequency
        }else{
            holder.freq.text = geo.start_date + "-" + geo.end_date
        }

        holder.point.text = geo.reward

        holder.delete.setOnClickListener {

            if(geo.type!!.equals("task_")) {
//                var bundle = bundleOf("task_id" to geo.id,"parenttoken" to geo.user_device_token)
//                navController!!.navigate(R.id.taskDetailFragment, bundle)
            }else{
                subject_id = geo.subject_id
//                var bundle = bundleOf("task_id" to geo.id,"parenttoken" to geo.user_device_token)
//                navController!!.navigate(R.id.workSheetChildFragment, bundle)


                if(TaskChildFragment.not_detail != null){
                    TaskChildFragment.not_detail!!.deletetask(geo.id.toString())
                }

            }






        }

        holder.layout.setOnClickListener {
            if(geo.type!!.equals("task_")) {
                var bundle = bundleOf("task_id" to geo.id,"parenttoken" to geo.user_device_token)
                navController!!.navigate(R.id.taskDetailFragment, bundle)
            }else{
                subject_id = geo.subject_id
                var bundle = bundleOf("task_id" to geo.id,"parenttoken" to geo.user_device_token)
                navController!!.navigate(R.id.workSheetChildFragment, bundle)


            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layout = view.findViewById<LinearLayoutCompat>(R.id.contMain)
        var des = view.findViewById<TextView>(R.id.des)
        var freq = view.findViewById<TextView>(R.id.freq)
        var point = view.findViewById<TextView>(R.id.point)


        var delete = view.findViewById<ImageView>(R.id.delete)

    }

}