package com.appsnado.haippNew.Blocksmartschapps

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.Firebase.Model
import com.appsnado.haippNew.R
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set


class SmartAppBlockerAdapter(
        var mainActivity: Activity,
        var appBlockerArrayList: ArrayList<Model>?
) :
    RecyclerView.Adapter<SmartAppBlockerAdapter.ViewHolder>() {
    var images: IntArray? = intArrayOf(R.drawable.facebook, R.drawable.facetime,R.drawable.insta,R.drawable.linkdin,R.drawable.skype,R.drawable.messenger,R.drawable.snapchat,R.drawable.twiiter,R.drawable.whatsapp,R.drawable.netflix,R.drawable.linkdin,R.drawable.netflix)
    private val valueslist: java.util.ArrayList<Model>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainActivity).inflate(
                R.layout.item_app_blocker,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return appBlockerArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val item: HashMap<Any,Any> = appBlockerArrayList!!.get(position)
        try {
            holder.tvAppBlocker.setText(item.get("appname").toString())
            if (item.get("icon").toString() != null) if (!item.get("icon").toString().equals("")) {
                Picasso.get().load(item.get("icon").toString()).into(holder.img)
            }
            if (item.get("isLocked").toString().equals("true")) {
                holder.Switchimg.setChecked(false)
            } else {
                holder.Switchimg.setChecked(true)
            }
        } catch (e: Exception) {
            e.stackTrace
        }

      //  holder.tvAppBlocker.setOnClickListener(View.OnClickListener { remove(position) })

    //    holder.img.setImageResource(images!!.get(position))
       // holder.tvAppBlocker.text= appBlockerArrayList!!.get(position);


        holder.Switchimg.setOnClickListener {

            if(holder.Switchimg.isChecked){
                val apps: java.util.HashMap<Any, Any> = Model()
                apps["pacakagename"] = item["pacakagename"].toString()
                apps["isLocked"] = "false"
                apps["icon"] = item["icon"].toString()
                apps["appname"] = item["appname"].toString()
                appBlockerArrayList!!.set(position, apps as Model)
                if(SmartAppBlockerFragment.not_detail != null){
                    SmartAppBlockerFragment.not_detail!!.sendmessage(position,
                        item.get("pacakagename").toString(),appBlockerArrayList!!,holder.Switchimg.isChecked.toString(),item["pacakagename"].toString(),item["appname"].toString())

                }
            }else{
                val apps: java.util.HashMap<Any, Any> = Model()
                apps["pacakagename"] = item["pacakagename"].toString()
                apps["isLocked"] = "true"
                apps["icon"] = item["icon"].toString()
                apps["appname"] = item["appname"].toString()
                appBlockerArrayList!!.set(position, apps as Model)

                if(SmartAppBlockerFragment.not_detail != null){
                    SmartAppBlockerFragment.not_detail!!.sendmessage(position,
                        item.get("pacakagename").toString(),appBlockerArrayList!!,holder.Switchimg.isChecked.toString(),item["pacakagename"].toString(),item["appname"].toString())

                }
            }





//            if(AppBlockerFragment.not_detail != null){
//                AppBlockerFragment.not_detail!!.send(position,appBlockerArrayList!!.get(position).packageName)
//
//            }



        }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvAppBlocker = view.findViewById<TextView>(R.id.tvAppBlocker)
        var img = view.findViewById<ImageView>(R.id.imgview)
        var Switchimg = view.findViewById<SwitchCompat>(R.id.Switchimg)



    }
}
