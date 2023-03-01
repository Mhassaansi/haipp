package com.appsnado.haippNew.Feature

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.Comman_Pacakges.data.DbContract
import com.appsnado.haippNew.R
import com.appsnado.haippNew.data.SharedPreferenceManager

class FeatureAdapter(var mainact: Activity, var featuresArrayList: ArrayList<String>?) : RecyclerView.Adapter<FeatureAdapter.ViewHolder>() {
    var images: IntArray? = intArrayOf(R.drawable.activityreport, R.drawable.appblocker,R.drawable.browsinghistory,R.drawable.geofences,R.drawable.locationhistory,R.drawable.smartschdule,R.drawable.task,R.drawable.worksheetview,R.drawable.webfilters,R.drawable.uninstallpermisiion,R.drawable.lockmobile,R.drawable.ic_logout)

    var preferenceManager: SharedPreferenceManager? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainact).inflate(R.layout.item_features, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return featuresArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvFeatures.text= featuresArrayList!!.get(position)
        holder.img.setImageResource(images!!.get(position))
        preferenceManager = SharedPreferenceManager(mainact)

        if(featuresArrayList!!.get(position).equals("Uninstall Permission")){
            if (DbContract.install.equals("true")) {
                holder.switch.setChecked(true)
            } else {
                holder.switch.setChecked(false)
            }
            holder.switch.visibility = View.VISIBLE
        } else if(featuresArrayList!!.get(position).equals("Lock Mobile")){
            if (DbContract.lockvalue.equals("true")) {
                holder.switch.setChecked(true)
            } else {
                holder.switch.setChecked(false)
            }
            holder.switch.visibility = View.VISIBLE
        }else if(featuresArrayList!!.get(position).equals("Child Logout Permission")){
            if (DbContract.logoutpermission.equals("true")) {
                holder.switch.setChecked(true)
            } else {
                holder.switch.setChecked(false)
            }
            holder.switch.visibility = View.VISIBLE
        }else {
            holder.switch.visibility = View.GONE
        }



                holder.switch.setOnClickListener { view: View ->
                   // if (holder.switch.isChecked()) {
                        if(featuresArrayList!!.get(position).equals("Lock Mobile")) {
                            if (FeatureFragment.not_detail != null)
                                FeatureFragment.not_detail!!.datafirebase(0,holder.switch.isChecked())
                        }else if(featuresArrayList!!.get(position).equals("Child Logout Permission")){
                            if (FeatureFragment.not_detail != null)
                                FeatureFragment.not_detail!!.datafirebase(2,holder.switch.isChecked())
                        }else {
                            if (FeatureFragment.not_detail != null)
                                FeatureFragment.not_detail!!.datafirebase(1,holder.switch.isChecked())
                        }
                    ///}
                }
       // mainActivity?.preferenceManager?.getdevicesid()
        holder.layout.setOnClickListener { view: View ->
            when( holder.tvFeatures.text){
                "Activity Report" -> {
                    if(FeatureFragment.selectchild) {
                        if (preferenceManager?.getdevicesid() != null) {
                            if (preferenceManager?.getdevicesid() != "")
                                navController!!.navigate(R.id.reportFragment)
                        }
                    }else{
                        Toast.makeText(mainact,"Please select kid devices",Toast.LENGTH_LONG).show()
                    }
                }
                "App Blocker" -> {
                    if(FeatureFragment.selectchild) {
                        if (DbContract.childtoken != "") {
                            navController!!.navigate(R.id.appBlockerFragment)
                        } else {
                            Toast.makeText(mainact, "Please select kid devices", Toast.LENGTH_LONG).show()
                        }
                    }else{
                        Toast.makeText(mainact,"Please select kid devices",Toast.LENGTH_LONG).show()
                    }


                }
                "Browsing History" -> {
                    if(FeatureFragment.selectchild) {
                    if(preferenceManager?.getdevicesid() != null) {
                        if(preferenceManager?.getdevicesid() != "")
                        navController!!.navigate(R.id.browsingHistoryFragment)
                    }
                    }else{
                        Toast.makeText(mainact,"Please select kid devices",Toast.LENGTH_LONG).show()
                    }
                }
                "Geo Fences" -> {
                    if(FeatureFragment.selectchild) {
                    if(preferenceManager?.getdevicesid() != null) {
                        if(preferenceManager?.getdevicesid() != "")
                        navController!!.navigate(R.id.geoFencesFragment)
                    }
                    }else{
                        Toast.makeText(mainact,"Please select kid devices",Toast.LENGTH_LONG).show()
                    }
                }
                "Task" -> {
                    if(FeatureFragment.selectchild) {
                    if(preferenceManager?.getdevicesid() != null) {
                        if(preferenceManager?.getdevicesid() != "")
                        navController!!.navigate(R.id.taskFragment)

                    }
                    }else{
                        Toast.makeText(mainact,"Please select kid devices",Toast.LENGTH_LONG).show()
                    }
                }
                "Location History" -> {
                    if(FeatureFragment.selectchild) {
                    if(preferenceManager?.getdevicesid() != null) {
                        if(preferenceManager?.getdevicesid() != "")
                        navController!!.navigate(R.id.locationhistroyFragment)
                    }
                    }else{
                        Toast.makeText(mainact,"Please select kid devices",Toast.LENGTH_LONG).show()
                    }
                }
                "Web Filters" -> {
                    if(FeatureFragment.selectchild) {
                    if(preferenceManager?.getdevicesid() != null) {
                        if(preferenceManager?.getdevicesid() != "")
                        navController!!.navigate(R.id.webfilterFragment)
                    }
                    }else{
                        Toast.makeText(mainact,"Please select kid devices",Toast.LENGTH_LONG).show()
                    }
                }
                "Smart Schedule" -> {
                    if(FeatureFragment.selectchild) {
                    if(preferenceManager?.getdevicesid() != null) {
                        if(preferenceManager?.getdevicesid() != "")
                        navController!!.navigate(R.id.smartscheduleFragment2)
                    }
                    }else{
                        Toast.makeText(mainact,"Please select kid devices",Toast.LENGTH_LONG).show()
                    }
                }
                "Work Sheet" -> {
                    if(FeatureFragment.selectchild) {
                    if(preferenceManager?.getdevicesid() != null) {
                        if(preferenceManager?.getdevicesid() != "")
                        navController!!.navigate(R.id.worksheetFragment)
                    }
                    }else{
                        Toast.makeText(mainact,"Please select kid devices",Toast.LENGTH_LONG).show()
                    }
                }

            }

            }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvFeatures = view.findViewById<TextView>(R.id.tvFeatures)
        var layout = view.findViewById<CardView>(R.id.contMain)
        var img = view.findViewById<ImageView>(R.id.imgview)
        var switch = view.findViewById<SwitchCompat>(R.id.permission)
    }
}