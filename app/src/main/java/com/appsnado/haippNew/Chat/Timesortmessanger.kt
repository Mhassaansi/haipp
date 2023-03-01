package com.appsnado.haippNew.Chat

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Timesortmessanger : Comparator<Messangermodel?> {
    var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    override fun compare(o1: Messangermodel?, o2: Messangermodel?): Int {
        return try {
            val valu = o1!!.getcreatedAt().toString().toLong()
            val valu2 = o2!!.getcreatedAt().toString().toLong()
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