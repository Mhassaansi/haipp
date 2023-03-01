package com.appsnado.haippNew.Webfilter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.R

class WebfilterAdapter(var mainActivity: Activity, var appBlockerArrayList: ArrayList<String>?) :
    RecyclerView.Adapter<WebfilterAdapter.ViewHolder>() {
    var images: IntArray? = intArrayOf(R.drawable.facebook, R.drawable.facetime,R.drawable.insta,R.drawable.linkdin,R.drawable.skype,R.drawable.messenger,R.drawable.snapchat,R.drawable.twiiter,R.drawable.whatsapp,R.drawable.netflix,R.drawable.linkdin,R.drawable.netflix)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainActivity).inflate(
                R.layout.item_webfilter,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return appBlockerArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // holder.img.setImageResource(images!!.get(position))
      //  holder.tvAppBlocker.text= appBlockerArrayList!!.get(position)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
       // var tvAppBlocker = view.findViewById<TextView>(R.id.tvAppBlocker)
      //  var img = view.findViewById<ImageView>(R.id.imgview)
    }
}
