package com.appsnado.haippNew.Worksheet

import android.app.Activity
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.R
import com.appsnado.haippNew.Smartschedule.WorksheetFragment.Companion.subject_id
import com.appsnado.haippNew.Worksheet.WorkSheetChildFragment.Companion.obj
import com.appsnado.haippNew.data.SharedPreferenceManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class WorkSheetChildAdapter(
    var mainact: Activity,
    var taskArrayList: ArrayList<Questionmodel>?
) :
    RecyclerView.Adapter<WorkSheetChildAdapter.ViewHolder>() {
    var preferenceManager: SharedPreferenceManager? = null
    var answer :String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainact).inflate(R.layout.item_worklist_child, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return taskArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        preferenceManager = SharedPreferenceManager(mainact)

        var geo  = taskArrayList!!.get(position)

        holder.textquest.text = geo.question_question
        holder.op1.text = geo.question_option_1
        holder.op2.text = geo.question_option_2
        holder.op3.text = geo.question_option_3
        answer = geo.question_correct_answer.toString()

        if(preferenceManager!!.gettype() != "parent"){
            val option = JSONArray()

            holder.op1.setOnClickListener {
                 obj = JSONObject()
                try {
                    holder.op1.setBackgroundTintList(ColorStateList.valueOf(mainact.resources.getColor(R.color.coloraccent)))
                    holder.op2.setBackgroundTintList(ColorStateList.valueOf(mainact.resources.getColor(R.color.yellowcolorapp)))
                    holder.op3.setBackgroundTintList(ColorStateList.valueOf(mainact.resources.getColor(R.color.yellowcolorapp)))

                    obj.put("id", geo.question_id)
                    obj.put("question_option", geo.question_option_1)
                } catch (e: JSONException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
                option.put(obj)
                if(WorkSheetChildFragment.not_detail != null){
                    WorkSheetChildFragment!!.not_detail!!.addjson(preferenceManager!!.getdevicesid(),subject_id,obj, geo.question_option_1.toString(),
                        answer!!
                    )
                }

            }
            holder.op2.setOnClickListener {

                holder.op1.setBackgroundTintList(ColorStateList.valueOf(mainact.resources.getColor(R.color.yellowcolorapp)))
                holder.op2.setBackgroundTintList(ColorStateList.valueOf(mainact.resources.getColor(R.color.coloraccent)))
                holder.op3.setBackgroundTintList(ColorStateList.valueOf(mainact.resources.getColor(R.color.yellowcolorapp)))
                val option = JSONArray()

                 obj = JSONObject()
                try {

                    obj.put("id", geo.question_id)
                    obj.put("question_option", geo.question_option_2)
                } catch (e: JSONException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
                option.put(obj)
                if(WorkSheetChildFragment.not_detail != null){
                    WorkSheetChildFragment!!.not_detail!!.addjson(preferenceManager!!.getdevicesid(),subject_id,obj,geo.question_option_2.toString(),
                        answer!!
                    )
                }
            }
            holder.op3.setOnClickListener {

                holder.op1.setBackgroundTintList(ColorStateList.valueOf(mainact.resources.getColor(R.color.yellowcolorapp)))
                holder.op2.setBackgroundTintList(ColorStateList.valueOf(mainact.resources.getColor(R.color.yellowcolorapp)))
                holder.op3.setBackgroundTintList(ColorStateList.valueOf(mainact.resources.getColor(R.color.coloraccent)))

                val option = JSONArray()
                   obj = JSONObject()
                try {

                    obj.put("id", geo.question_id)
                    obj.put("question_option", geo.question_option_3)


                } catch (e: JSONException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
                option.put(obj)
                if(WorkSheetChildFragment.not_detail != null){
                    WorkSheetChildFragment!!.not_detail!!.addjson(preferenceManager!!.getdevicesid(),subject_id,obj,geo.question_option_3.toString(),
                        answer!!
                    )
                }
            }
        }




//        if (position % 2 == 0) {
//            holder.layout1.visibility = View.VISIBLE
//        } else {
//            holder.layout2.visibility = View.VISIBLE
//        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layout1 = view.findViewById<LinearLayoutCompat>(R.id.layout1)
        var layout2 = view.findViewById<LinearLayoutCompat>(R.id.layout2)

        var textquest = view.findViewById<TextView>(R.id.question)
        var op1 = view.findViewById<TextView>(R.id.opt1)
        var op2 = view.findViewById<TextView>(R.id.opt2)
        var op3 = view.findViewById<TextView>(R.id.opt3)

    }

}