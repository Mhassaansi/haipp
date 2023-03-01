package com.appsnado.haippNew.Kiddevices

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.R
import com.appsnado.haippNew.Smartschedule.WorksheetFragment.Companion.subject_id

class ListworkAdapter(var mainact: Activity,
                     var kiddevicesArrayList: ArrayList<String>?
) :
    RecyclerView.Adapter<ListworkAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainact).inflate(R.layout.item_worklist, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return kiddevicesArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvworksheet.text= kiddevicesArrayList!!.get(position)
        holder.layout.setOnClickListener { view: View ->
                        if(subject_id != ""){
                            navController.popBackStack(R.id.listworkFragment, true);
                            navController!!.navigate(R.id.workSheetChildFragment)
                    }

//            when( holder.tvFeatures.text){
//                "App Blocker" -> MainActivity.navController!!.navigate(R.id.appBlockerFragment)
//                "Browsing History" -> MainActivity.navController!!.navigate(R.id.browsingHistoryFragment)
//                "Geo Fences" -> MainActivity.navController!!.navigate(R.id.geoFencesFragment)
//            }

        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvworksheet = view.findViewById<TextView>(R.id.tvworksheet)
        var layout = view.findViewById<LinearLayout>(R.id.layout)



    }
}