package com.appsnado.haippNew.Adddevices

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.R
import com.appsnado.haippNew.data.SharedPreferenceManager
import com.google.gson.Gson

class AdddevicesAdapter(var mainact: Activity,
                         var kiddevicesArrayList: ArrayList<Adddevicesmodel>?
) :


        RecyclerView.Adapter<AdddevicesAdapter.ViewHolder>() {
    var preferenceManager: SharedPreferenceManager? = null
    private var btnstate :Boolean = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mainact).inflate(R.layout.item_add_devices, parent, false)
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
        holder.layout.setOnClickListener { view: View ->
            if(!btnstate) {
                preferenceManager = SharedPreferenceManager(mainact)
                preferenceManager!!.setdevicesid(getLayoutResponseMapper.devicesid)
                preferenceManager!!.setdevicetitle(getLayoutResponseMapper.device_title)

                btnstate = true
                if (AdddevicesFragment.not_detail != null)
                    AdddevicesFragment.not_detail!!.firebase(
                        getLayoutResponseMapper.devicesid,
                        getLayoutResponseMapper.device_title
                    );


            }
                  // navController!!.navigate(R.id.homeFragment)



//            when( holder.tvFeatures.text){
//                "App Blocker" -> MainActivity.navController!!.navigate(R.id.appBlockerFragment)
//                "Browsing History" -> MainActivity.navController!!.navigate(R.id.browsingHistoryFragment)
//                "Geo Fences" -> MainActivity.navController!!.navigate(R.id.geoFencesFragment)
//            }

        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvFeatures = view.findViewById<TextView>(R.id.tvFeatures)
        var layout = view.findViewById<LinearLayout>(R.id.one)



    }
}