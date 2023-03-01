package com.appsnado.haippNew.taskChild

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.R
import com.appsnado.haippNew.data.SharedPreferenceManager
import com.appsnado.haippNew.taskParent.claimModel
import java.util.ArrayList

class ClaimAdapter(
        var mainact: Activity,
        var taskArrayList: ArrayList<claimModel>?
) :
    RecyclerView.Adapter<ClaimAdapter.ViewHolder>() {
    var preferenceManag: SharedPreferenceManager? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainact).inflate(R.layout.item_claim, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return taskArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        preferenceManag = SharedPreferenceManager(mainact)
        var geo  = taskArrayList!!.get(position)

//        holder.des.text = geo.task_description
//        holder.time.text = geo.task_start_date + "-" + geo.task_end_date
//        holder.point.text = geo.task_reward


        holder.des.text = geo.description
        holder.time.text = geo.start_date + "-" + geo.end_date

        holder.point.text = geo.reward

        if(geo.reward != null){
            holder.claimid.text = "Claimed"
        }

        holder.layout.setOnClickListener {

            if (!preferenceManag!!.getstarttime()!!) {
                if (geo.reward == null) {

                    if (geo.type.equals("ts_")) {
                        if (ClaimFragment.not_detail != null) {
                            ClaimFragment!!.not_detail!!.calldailog(
                                geo.id.toString(),
                                geo.task_reward,
                                "ts_"
                            )
                        }
                    } else {
                        if (ClaimFragment.not_detail != null) {
                            ClaimFragment!!.not_detail!!.calldailog(
                                geo.id.toString(),
                                geo.task_reward,
                                "ws_"
                            )
                        }
                    }

                }
            }else{
                  Toast.makeText(mainact,"First Stop Time",Toast.LENGTH_LONG).show()
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layout = view.findViewById<LinearLayoutCompat>(R.id.claim)

        var des = view.findViewById<TextView>(R.id.des)
        var time = view.findViewById<TextView>(R.id.sdate)
        var point = view.findViewById<TextView>(R.id.point)
        var claimid = view.findViewById<TextView>(R.id.claimim)
    }

}