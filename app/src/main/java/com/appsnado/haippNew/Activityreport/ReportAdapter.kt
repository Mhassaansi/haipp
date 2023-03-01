package com.appsnado.haippNew.Activityreport

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.Firebase.Model
import com.appsnado.haippNew.R
import com.squareup.picasso.Picasso

class ReportAdapter(
        var mainActivity: Activity,
        var browsingArrayList: ArrayList<Model>?
) :
    RecyclerView.Adapter<ReportAdapter.ViewHolder>() {
    var images: IntArray? = intArrayOf(
        R.drawable.facebook, R.drawable.facetime,
        R.drawable.insta,
        R.drawable.linkdin,
        R.drawable.skype,
        R.drawable.messenger,
        R.drawable.snapchat,
        R.drawable.twiiter,
        R.drawable.whatsapp,
        R.drawable.netflix,
        R.drawable.linkdin,
        R.drawable.netflix)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainActivity).inflate(
                R.layout.item_browsing_history,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return browsingArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: HashMap<Any,Any> = browsingArrayList!!.get(position)
        //holder.img.setImageResource(images!!.get(position))

        var time = formatDuration(item.get("totaltime").toString().toLong())
        Log.i("TAG", "onBindViewHolder: "+time)
        holder.timetv.setText(time)
        holder.tvHistory.setText(item.get("appname").toString())
        Picasso.get().load(item.get("icon").toString()).into(holder.img)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvHistory = view.findViewById<TextView>(R.id.tvHistory)
        var img = view.findViewById<ImageView>(R.id.imgview)

        var timetv = view.findViewById<TextView>(R.id.time)


    }

    fun formatDuration(millis: Long): String? {
        val seconds = millis / 1000 % 60
        val minutes = millis / (1000 * 60) % 60
        val hours = millis / (1000 * 60 * 60)
        val b = StringBuilder()
        b.append(if (hours == 0L) "00" else if (hours < 10) "0$hours" else hours.toString())
        b.append(":")
        b.append(if (minutes == 0L) "00" else if (minutes < 10) "0$minutes" else minutes.toString())
        b.append(":")
        b.append(if (seconds == 0L) "00" else if (seconds < 10) "0$seconds" else seconds.toString())
        return b.toString()
    }

    fun convertSecondsToHMmSs(seconds: Long): String? {
        val s = seconds % 60
        val m = seconds / 60 % 60
        val h = seconds / (60 * 60) % 24
        return String.format("%d:%02d:%02d", h, m, s)
    }
}