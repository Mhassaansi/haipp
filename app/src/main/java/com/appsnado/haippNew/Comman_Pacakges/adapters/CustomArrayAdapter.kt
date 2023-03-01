package com.appsnado.haippNew.Comman_Pacakges.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.appsnado.haippNew.R


class CustomArrayAdapter(var context: Context?, var Array: Array<String?>) : BaseAdapter() {
    var inflter: LayoutInflater?
    override fun getCount(): Int {
        return Array!!.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View? {
        var view = view
        view = inflter!!.inflate(R.layout.spinner_item, null)
        //val icon = view?.findViewById<View?>(R.id.imageView) as ImageView
//        val gson = Gson()
//        val s2: String = gson.toJson( Array!!.get(i))
//        val getLayoutResponseMapper: Adddevicesmodel = Gson().fromJson(s2, Adddevicesmodel::class.java)
        val names = view?.findViewById<View?>(R.id.spItem) as TextView
        //icon.setImageResource(images!!.get(i))
        names.text =Array?.get(i)

        return view


    }

    init {
        inflter = LayoutInflater.from(context)
    }
}