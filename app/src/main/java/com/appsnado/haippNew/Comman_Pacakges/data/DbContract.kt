package com.appsnado.haippNew.Comman_Pacakges.data

import com.appsnado.haippNew.Adddevices.Adddevicesmodel

class DbContract {

    companion object {

        val SYNC_STATUS_OK = 0
        val SYNC_STATUS_FAILED = 1
        val TABLE_NAME = "Karve_credit_card"
         val Homenav = "0"
        var lockvalue: String = "false"
        var install :String =  "false"
        var logoutpermission :String =  "false"
        var childtoken :String =  ""
        val devices: ArrayList<String?> = ArrayList()
        var newsItems = ArrayList<Adddevicesmodel>()

    }
}