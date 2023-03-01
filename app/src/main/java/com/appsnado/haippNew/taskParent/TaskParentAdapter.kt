package com.appsnado.haippNew.taskParent

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.MainActivity
import com.appsnado.haippNew.R

class TaskParentAdapter(var mainact: Activity,
                        var taskArrayList: ArrayList<AddtaskModelParent>?
) :
    RecyclerView.Adapter<TaskParentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainact).inflate(R.layout.item_parent_task, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return taskArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

      ///  AddtaskModel
        var geo  = taskArrayList!!.get(position)

         holder.des.text = geo.task_description
         holder.freq.text = geo.task_frequency
         holder.point.text = geo.task_reward


        holder.layout.setOnClickListener {
            var bundle = bundleOf("task_id" to geo.task_id,"parenttoken" to "")
            navController!!.navigate(R.id.taskDetailFragment,bundle)

        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layout = view.findViewById<LinearLayoutCompat>(R.id.contMain)
        var des = view.findViewById<TextView>(R.id.des)
        var freq = view.findViewById<TextView>(R.id.freq)
        var point = view.findViewById<TextView>(R.id.point)
    }

}