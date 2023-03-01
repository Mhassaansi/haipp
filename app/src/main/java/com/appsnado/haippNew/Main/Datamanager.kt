package com.appsnado.haippNew.Main

import android.app.Activity
import com.appsnado.haippNew.retro.WebServiceFactory
import com.appsnado.haippNew.retro.WebServiceFactory.Companion.getInstance


class Datamanager {
    fun getservices(activity: Activity?): WebServiceFactory? {
        return getInstance(activity!!)
    }
    companion object {
        private var sInstance: Datamanager? = null

        @Synchronized
        fun getInstance(): Datamanager? {
            if (sInstance == null) {
                sInstance = Datamanager()
            }
            return sInstance
        }
    }
}