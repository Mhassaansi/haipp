package com.appsnado.haippNew.GeoFences

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.Kiddevices.KiddevicesFragment
import com.appsnado.haippNew.R

class GeoFencesAdapter (
    var mainActivity: Activity
) :
    RecyclerView.Adapter<GeoFencesAdapter.ViewHolder>() {

    var mGeoFenceArrayList: ArrayList<GeoFencesModel>? = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainActivity).inflate(
                R.layout.item_geo_fences,
                parent,
                false
            )
        )
    }

    fun addAll(geoFenceArrayList: ArrayList<GeoFencesModel>)
    {
        mGeoFenceArrayList!!.clear()
        mGeoFenceArrayList!!.addAll(geoFenceArrayList)
    }


    override fun getItemCount(): Int {
        return mGeoFenceArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            var geo  = mGeoFenceArrayList!!.get(position)


                 holder.tvhome.text = geo.dc_title
                holder.address.text = geo.dc_address
                holder.miles.text = geo.dc_radius
        holder.deletimg.setOnClickListener { view: View ->

            if(GeoFencesFragment.not_detail != null){
                GeoFencesFragment.not_detail!!.detail(geo.dc_id)
            }

        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {



        var tvhome = view.findViewById<TextView>(R.id.home)
        var address = view.findViewById<TextView>(R.id.addres)
        var miles = view.findViewById<TextView>(R.id.tvmiles)
        var deletimg = view.findViewById<ImageView>(R.id.delet)

    }
}
