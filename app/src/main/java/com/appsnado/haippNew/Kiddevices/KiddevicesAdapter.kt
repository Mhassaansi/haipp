package com.appsnado.haippNew.Kiddevices

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.Adddevices.Adddevicesmodel
import com.appsnado.haippNew.R
import com.google.gson.Gson


class KiddevicesAdapter(var mainact: Activity,
                     var kiddevicesArrayList: ArrayList<Adddevicesmodel>?
) :
    RecyclerView.Adapter<KiddevicesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainact).inflate(R.layout.item_kids, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return kiddevicesArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gson = Gson()
        val s2: String = gson.toJson(kiddevicesArrayList!!.get(position))
        val getLayoutResponseMapper: Adddevicesmodel = Gson().fromJson(s2, Adddevicesmodel::class.java)

     //   var adddevicesmodel  =  getLayoutResponseMapper!!.get(position)
        holder.tvFeatures.text= getLayoutResponseMapper.device_title
        holder.deletimg.setOnClickListener { view: View ->

                 if(KiddevicesFragment.not_detail != null){
                     KiddevicesFragment.not_detail!!.detail(getLayoutResponseMapper.devicesid,getLayoutResponseMapper.device_title)
                 }



//            when( holder.tvFeatures.text){
//                "App Blocker" -> MainActivity.navController!!.navigate(R.id.appBlockerFragment)
//                "Browsing History" -> MainActivity.navController!!.navigate(R.id.browsingHistoryFragment)
//                "Geo Fences" -> MainActivity.navController!!.navigate(R.id.geoFencesFragment)
//            }

        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvFeatures = view.findViewById<TextView>(R.id.tvFeatures)
        var deletimg = view.findViewById<ImageView>(R.id.delet)



    }
}