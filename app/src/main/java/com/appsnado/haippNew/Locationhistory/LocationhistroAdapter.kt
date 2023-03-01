package com.appsnado.haippNew.Locationhistory

import android.app.Activity
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.R
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class LocationhistroAdapter(
    var mainActivity: Activity) :

    RecyclerView.Adapter<LocationhistroAdapter.ViewHolder>() {

    var browsingArrayList: ArrayList<locationhistroyModel> = ArrayList()

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
                R.layout.item_location_history,
                parent,
                false
            )
        )
    }

    fun addAll(geoFenceArrayList: ArrayList<locationhistroyModel>)
    {
        browsingArrayList!!.clear()
        browsingArrayList!!.addAll(geoFenceArrayList)
    }

    override fun getItemCount(): Int {
        return browsingArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var geo  = browsingArrayList!!.get(position)
            UIHelper.showprogress(mainActivity)
            var timelocation = geo.created_at!!.subSequence(0, 10).toString()
             holder.locationtime.text = timelocation
           var name = getAddress( geo.cc_latitude!!.toDouble(), geo.cc_longitude!!.toDouble())
           holder.tvHistory.text = name
            UIHelper.hidedailog()
      //  holder.img.setImageResource(images!!.get(position))
      //  holder.tvHistory.text = browsingArrayList!!.get(position)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
       var tvHistory = view.findViewById<TextView>(R.id.places)
        var locationtime = view.findViewById<TextView>(R.id.timelocation)

    }

    fun getAddress(lat: Double, lng: Double) :String{
        val geocoder = Geocoder(mainActivity, Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(lat, lng, 1)
            val obj: Address = addresses[0]

            var add: String = obj.getAddressLine(0)
            return add

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }
}