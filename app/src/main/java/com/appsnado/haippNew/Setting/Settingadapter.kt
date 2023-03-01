package com.appsnado.haippNew.Setting

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.Activities.MainActivity
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.R
import java.util.ArrayList

class Settingadapter(var mainact: Activity, var featuresArrayList: ArrayList<String>?) : RecyclerView.Adapter<Settingadapter.ViewHolder>() {
    var images: IntArray? = intArrayOf(R.drawable.ic_kids_device, R.drawable.ic_subscription, R.drawable.ic_change_email, R.drawable.ic_change, R.drawable.ic_terms, R.drawable.ic_privacy_policy, R.drawable.ic_faqs, R.drawable.ic_logout)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mainact).inflate(R.layout.settingadapter, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return featuresArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvFeatures.text = featuresArrayList!!.get(position)
        holder.img.setImageResource(images!!.get(position))
        holder.layout.setOnClickListener { view: View ->
            when (holder.tvFeatures.text) {
                "Kids Devices" -> navController!!.navigate(R.id.kiddevicesFragment)
                "Subscription" -> navController!!.navigate(R.id.subscriptionFragment)
                "Change Email" -> navController!!.navigate(R.id.changeemailFragment)
                "Change Password" -> {
                    var bundle = bundleOf("changepass" to "0")
                    navController!!.navigate(R.id.changepasswordFragment, bundle)
                }
                "Term & Condition" -> navController!!.navigate(R.id.termsandconditionFragment)
                "Privacy Policy" -> navController!!.navigate(R.id.privacyFragment)
                "FAQS" -> navController!!.navigate(R.id.faqFragment)
                "Logout" -> {
                    if (MainActivity.not_detail != null) {
                        MainActivity!!.not_detail?.logoutdailog()
                    }
                }


            }

        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvFeatures = view.findViewById<TextView>(R.id.settext)
        var layout = view.findViewById<LinearLayout>(R.id.layout)
        var img = view.findViewById<ImageView>(R.id.img)


    }
}