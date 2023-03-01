package com.appsnado.haippNew

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class Spinneradaptercustom(var context: Context?, var Arrayitems: Array<String?>?) : BaseAdapter() {
    var inflter: LayoutInflater?
    override fun getCount(): Int {
        return Arrayitems!!.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View? {
        var view = view
        view = inflter!!.inflate(R.layout.spinner_custom_layout_list, null)
       // val icon = view.findViewById<View?>(R.id.imageView) as ImageView
        val names = view!!.findViewById<View?>(R.id.textView) as TextView
      //  icon.setImageResource(images!!.get(i))
        names.text = Arrayitems!!.get(i)
//        if (i == 0) {
//          //  icon.visibility = View.GONE
//            names.visibility = View.GONE
//        } else {
//            //icon.visibility = View.VISIBLE
//            names.visibility = View.VISIBLE
//        }

        return view


    }

    init {
        inflter = LayoutInflater.from(context)
    }
}