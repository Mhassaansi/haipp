package com.appsnado.haippNew.Chat

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.appsnado.haippNew.R
import com.appsnado.haippNew.data.SharedPreferenceManager
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class Chatsadapter(
    var context: Context?,
    mArrayList: ArrayList<FirebasetimeClass?>?,
    usertypedata: String?
) : BaseAdapter() {


    companion object {

    }

    var messes: MutableList<FirebasetimeClass?>? = ArrayList()
    var usertypedataadapter: String?
    var userID: String?
    var preferenceManager: SharedPreferenceManager? = null
    fun add(mess: FirebasetimeClass?) {
        messes!!.add(mess)
        notifyDataSetChanged()

    }


    override fun getCount(): Int {
        return messes!!.size
    }

    override fun getItem(i: Int): Any? {
        return messes!!.get(i)
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    fun updateData(mArrayList: ArrayList<Mess?>) {
      //  messes?.addAll(mArrayList!!)
        notifyDataSetChanged()
    }

    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup?): View? {
        var convertView = convertView
        val holder = MessageViewHolder()
        val messageInflater =
            context!!.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val message = messes!!.get(i)
        if (preferenceManager!!.gettype() == "parent") {
            userID = preferenceManager!!.getUser()!!.userID

        } else {
            userID = preferenceManager!!.getdevicesid()
        }
        // userId =  getMainActivity().preferenceManager.getuserid();
        // userId =  getMainActivity().preferenceManager.getuserid();
        if (message!!.getSenderID() != null) {
            if (message!!.getSenderID() != userID) {
                convertView = messageInflater.inflate(R.layout.my_message, null)
                holder.time = convertView.findViewById<View?>(R.id.time) as TextView
                holder.messageBody =
                    convertView.findViewById<View?>(R.id.tvOtherMessage) as TextView

                convertView.tag = holder
                holder.messageBody!!.setText(message!!.getContent())



                try {
                    // yyyy-MM-dd HH:mm:ss
                    val valu = message.getfirebasetime()!!.seconds!!.toLong() * 1000
                    //val sdf = SimpleDateFormat("hh:mm aa")
                    val sdf = SimpleDateFormat("dd MMM, yyyy HH:mm:ss")
                    val d = Date(valu)
                    val time = sdf.format(d)
                    holder.time!!.setText(time)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                convertView = messageInflater.inflate(R.layout.their_message, null)
                holder.messageBody = convertView.findViewById<View?>(R.id.tvMyMessage) as TextView
                holder.time = convertView.findViewById<View?>(R.id.timeo) as TextView
                convertView.tag = holder
                holder.messageBody!!.setText(message!!.getContent())

                try {

                    val valu = message.getfirebasetime()!!.seconds!!.toString().toLong()* 1000
                    //   val sdf = SimpleDateFormat("hh:mm aa")
                    val sdf = SimpleDateFormat("dd MMM, yyyy HH:mm:ss")
                    val d = Date(valu)
                    val time = sdf.format(d)
                    holder.time!!.setText(time)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                // GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();
                // drawable.setColor(Color.parseColor(message.getMemberData().getColor()));
            }
        }
        return convertView
    }

    init {
        messes = mArrayList
        userID = ""
        this.usertypedataadapter = usertypedata
        preferenceManager = SharedPreferenceManager(context)
    }
}


internal class MessageViewHolder {
    var avatar: View? = null
    var name: TextView? = null
    var img: CircleImageView? = null
    var messageBody: TextView? = null
    var time: TextView? = null

    var postlayout: RelativeLayout? = null

    var postchat: RelativeLayout? = null
    var greelayout: RelativeLayout? = null
    var post: TextView? = null
    var des: TextView? = null
    var imag: ImageView? = null
    var seenimg: ImageView? = null


    var postuser: CircleImageView? = null


    var imgattach: ImageView? = null
}
