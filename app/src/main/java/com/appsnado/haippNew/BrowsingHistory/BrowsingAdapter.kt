package com.appsnado.haippNew.BrowsingHistory

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.R
import java.util.*


class BrowsingAdapter(
        var mainActivity: Activity,
        var browsingArrayList: ArrayList<AddUrl>
) :
    RecyclerView.Adapter<BrowsingAdapter.ViewHolder>() {
    var images: IntArray? = intArrayOf(R.drawable.facebook, R.drawable.facetime,R.drawable.insta,R.drawable.linkdin,R.drawable.skype,R.drawable.messenger,R.drawable.snapchat,R.drawable.twiiter,R.drawable.whatsapp,R.drawable.netflix,R.drawable.linkdin,R.drawable.netflix)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainActivity).inflate(
                R.layout.item_report,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return browsingArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: AddUrl = browsingArrayList.get(position)
        val time: String = this!!.getDate(item.starttime.toString().toLong())!!
        //  holder.img.setImageResource(images!!.get(position))
        holder.tvHistory.text = item.url
        holder.time.text = time


        holder.tvHistory.setOnClickListener {

            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse(item.url.toString())
            mainActivity.startActivity(browserIntent)
        }



    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvHistory = view.findViewById<TextView>(R.id.tvHistory)
        var time = view.findViewById<TextView>(R.id.time)
        var img = view.findViewById<ImageView>(R.id.imgview)

    }

    private fun getDate(milliSeconds: Long): String? {
        // Create a DateFormatter object for displaying date in specified
        // format.
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        //Long time = System.currentTimeMillis();
        calendar.timeInMillis = milliSeconds

        //dd=day, MM=month, yyyy=year, hh=hour, mm=minute, ss=second.
        return DateFormat.format("dd-MM-yyyy hh:mm:ss", calendar).toString()
    }
}
