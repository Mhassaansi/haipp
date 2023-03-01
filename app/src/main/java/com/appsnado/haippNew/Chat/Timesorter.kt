package com.appsnado.haippNew.Chat

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Timesorter : Comparator<FirebasetimeClass?> {

    var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    override fun compare(o1: FirebasetimeClass?, o2: FirebasetimeClass?): Int {
        return try {



            val valu = o1!!.getfirebasetime()!!.seconds.toString().toLong() * 1000
            val valu2 = o2!!.getfirebasetime()!!.seconds.toString().toLong() * 1000
            val d = Date(valu)
            val d2 = Date(valu2)
            val d_format = simpleDateFormat.format(d)
            val d2_format = simpleDateFormat.format(d2)
            simpleDateFormat.parse(d_format).compareTo(simpleDateFormat.parse(d2_format))

        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
  }


}