package com.appsnado.haippNew.Chat

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.Adddevices.Adddevicesmodel
import com.appsnado.haippNew.R
import de.hdodenhof.circleimageview.CircleImageView

class ChatParentAdapter(
    var mainact: Activity,
    var taskArrayList: ArrayList<Adddevicesmodel>
) :
    RecyclerView.Adapter<ChatParentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainact).inflate(R.layout.item_chat, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return taskArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = taskArrayList[position]
        var geo = taskArrayList!!.get(position)
        holder.devicesname.text = geo.device_title
        with(holder) {
            bindTo(model, mainact!!)
            layout.setOnClickListener {

                if (ChatFragment.not_detail != null) {
                    taskArrayList?.forEach { it.isSelected = false }
                    taskArrayList?.get(position).isSelected = true
                    notifyDataSetChanged()
                    ChatFragment!!.not_detail?.callchild(geo.device_title, geo.devicesid)

                }

            }
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layout = view.findViewById<RelativeLayout>(R.id.contMain)
        var devicesname = view.findViewById<TextView>(R.id.name)
        var imgSelected = view.findViewById<CircleImageView>(R.id.imgSelected)
        var model: Adddevicesmodel? = null
        fun bindTo(model: Adddevicesmodel?, context: Context) {
            this.model = model
            this.model?.let {
                if (it.isSelected) {
                    imgSelected.visibility = View.VISIBLE
                } else {
                    imgSelected.visibility = View.GONE
                }
            }


        }
    }


}