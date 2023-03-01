package com.appsnado.haippNew.notification

import android.app.Activity
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.MainActivity
import com.appsnado.haippNew.R
import com.google.gson.Gson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Notificationadatpter(
    var mainact: Activity,
    var kiddevicesArrayList: ArrayList<Notificationmodel>?,
   var gettype: String?
) :
    RecyclerView.Adapter<Notificationadatpter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainact).inflate(R.layout.item_notification, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return kiddevicesArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gson = Gson()
        val s2: String = gson.toJson(kiddevicesArrayList!!.get(position))
        val getLayoutResponseMapper: Notificationmodel =
            Gson().fromJson(s2, Notificationmodel::class.java)

        //   var adddevicesmodel  =  getLayoutResponseMapper!!.get(position)
        holder.username.text = getLayoutResponseMapper.notification_title
        holder.message.text = getLayoutResponseMapper.notification_message

        try {


            holder.layout.setOnClickListener {

                if (gettype == "parent"){
                    if(getLayoutResponseMapper.notification_title.equals("Worksheet")){
//                        if(HomeFragment.not_det!! != null){
//                            HomeFragment!!.not_det!!.callworksheet()
//                        }

                        navController!!.navigate(R.id.worksheetFragment)

                    }else{
                        navController!!.navigate(R.id.taskFragment)
                    }

                }else{
                    navController!!.navigate(R.id.taskChildFragment)
                }
            }



//            val inputText =  getLayoutResponseMapper.created_at
//            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
//            inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"))
//            val outputFormat = SimpleDateFormat("dd MMM, yyyy hh:mm")
//            val date: Date = inputFormat.parse(inputText)
//            val outputText: String = outputFormat.format(date)
//            holder.time.text = outputText


            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            try {
                val date = df.parse(getLayoutResponseMapper.created_at)
                 var dateString = DateFormat.format("MMM d, yyyy, h:mm a", date)
                    .toString()
                holder.time.text = dateString
            } catch (e: ParseException) {
                e.printStackTrace()
            }






        } catch (e: Exception) {
            e.printStackTrace()
        }




    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var username = view.findViewById<TextView>(R.id.name)
        var message = view.findViewById<TextView>(R.id.message)
        var time = view.findViewById<TextView>(R.id.time)
        var layout = view.findViewById<LinearLayout>(R.id.one)


    }
}