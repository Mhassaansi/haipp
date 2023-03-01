package com.appsnado.haippNew.Chat

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appsnado.haippNew.R

class ChatMessageAdapter(
    var mainact: Activity,
    var taskArrayList: ArrayList<String>?
) :
    RecyclerView.Adapter<ChatMessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainact).inflate(R.layout.item_message, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return taskArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

}