package com.appsnado.haippNew.taskParent

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.R

class Dateadapter(
    var mainActivity: Activity,
    taskArrayList: java.util.ArrayList<String>?
) :
    RecyclerView.Adapter<Dateadapter.ViewHolder>() {

    var mGeoFenceArrayList: ArrayList<String>? = taskArrayList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainActivity).inflate(
                R.layout.itemcalander,
                parent,
                false
            )
        )
    }

    fun addAll(geoFenceArrayList: ArrayList<String>)
    {
        mGeoFenceArrayList!!.clear()
        mGeoFenceArrayList!!.addAll(geoFenceArrayList)
    }


    override fun getItemCount(): Int {
        return mGeoFenceArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

           holder.tvhome.setText(mGeoFenceArrayList?.get(position))
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {



        var tvhome = view.findViewById<TextView>(R.id.text)


//        var address = view.findViewById<TextView>(R.id.addres)
//        var miles = view.findViewById<TextView>(R.id.tvmiles)
//        var deletimg = view.findViewById<ImageView>(R.id.delet)

    }


}