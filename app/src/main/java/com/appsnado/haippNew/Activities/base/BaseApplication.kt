package com.appsnado.haippNew.base

import android.app.Application
import com.appsnado.haippNew.Applocakpacakges.LockApplication
import com.appsnado.haippNew.baseactivity.BaseActivity

class BaseApplication : Application() {

    private val application: LockApplication? = null
    private val activityList: List<BaseActivity>? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
    }



    companion object {
        private var instance: BaseApplication? = null
        fun getApplication(): BaseApplication? {
            return instance
        }
    }
}