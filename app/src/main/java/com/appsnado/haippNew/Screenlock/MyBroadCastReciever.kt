package com.appsnado.haippNew.Screenlock

import android.app.admin.DevicePolicyManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.appsnado.haippNew.Applocakpacakges.activities.lock.GestureSelfUnlockActivity
import com.appsnado.haippNew.Applocakpacakges.base.AppConstants
import com.appsnado.haippNew.Applocakpacakges.services.BackgroundManager
import com.appsnado.haippNew.Applocakpacakges.services.LockService
import com.appsnado.haippNew.DevAdminReceiver
import com.appsnado.haippNew.Smartschedule.SmartschedulModel
import com.appsnado.haippNew.Smartschedule.SmartschedulModelJava
import com.appsnado.haippNew.data.SharedPreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class MyBroadCastReciever : BroadcastReceiver() {
    private var preferenceManager: SharedPreferenceManager? = null
    var taskArrayList: ArrayList<SmartschedulModel?>? = null
    var dayof :String = ""
    private var lockkeystate = false
    override fun onReceive(context: Context, intent: Intent) {
        preferenceManager = SharedPreferenceManager(context)
        if (!BackgroundManager.getInstance().init(context).isServiceRunning(LockService::class.java)) {
            BackgroundManager.getInstance().init(context).startService(LockService::class.java)
        }
        BackgroundManager.getInstance().init(context).startAlarmManager()

        if (intent.action == Intent.ACTION_SCREEN_OFF) {
            Log.i("Check", "Screen went OFF")
           /// Toast.makeText(context, "screen OFF", Toast.LENGTH_LONG).show()
            if (preferenceManager!!.gettype() != "parent") {
                if (preferenceManager!!.getscreenlock() == true) {
                    val intent =
                            Intent(context, GestureSelfUnlockActivity::class.java)
                    intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, AppConstants.APP_PACKAGE_NAME)
                    intent.putExtra(
                            AppConstants.LOCK_FROM,
                            AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY
                    )
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent)

                    val mDPM = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                    val mDeviceAdminRcvr = ComponentName(context, DevAdminReceiver::class.java)
                    val active = mDPM.isAdminActive(mDeviceAdminRcvr)
                    if (active) {
                        mDPM.lockNow()
                    }

                }else{
                    Toast.makeText(context, "screen ON", Toast.LENGTH_LONG).show()
                    checkvaluew(context)
                }
            }
        } else if (intent.action == Intent.ACTION_SCREEN_ON) {
            if (preferenceManager!!.gettype() != "parent") {
                if (preferenceManager!!.getscreenlock() == true) {
                    val intent =
                            Intent(context, GestureSelfUnlockActivity::class.java)
                    intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, AppConstants.APP_PACKAGE_NAME)
                    intent.putExtra(
                            AppConstants.LOCK_FROM,
                            AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY
                    )
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent)

                    val mDPM = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                    val mDeviceAdminRcvr = ComponentName(context, DevAdminReceiver::class.java)
                    val active = mDPM.isAdminActive(mDeviceAdminRcvr)
                    if (active) {
                        mDPM.lockNow()
                    }
                } else {
                    Log.i("Check", "Screen went ON")
                    Toast.makeText(context, "screen ON", Toast.LENGTH_LONG).show()
                    checkvaluew(context)

                    /// // val gson = Gson()
                    //val strJson = "[{\"ss_id\":2.0,\"ss_device_id\":6.0,\"ss_title\":\"test test\",\"ss_day\":\"mon\",\"ss_start_time\":\"01:00:00\",\"ss_end_time\":\"02:00:00\",\"ss_is_blocked\":\"0\",\"created_at\":\"2021-08-03T11:55:45.000000Z\",\"updated_at\":\"2021-08-03T11:55:45.000000Z\"},{\"ss_id\":3.0,\"ss_device_id\":6.0,\"ss_title\":\"test test\",\"ss_day\":\"mon\",\"ss_start_time\":\"17:50:00\",\"ss_end_time\":\"18:50:00\",\"ss_is_blocked\":\"0\",\"created_at\":\"2021-08-03T13:54:50.000000Z\",\"updated_at\":\"2021-08-03T13:54:50.000000Z\"},{\"ss_id\":4.0,\"ss_device_id\":6.0,\"ss_title\":\"test\",\"ss_day\":\"Tuesday\",\"ss_start_time\":\"16:25:00\",\"ss_end_time\":\"21:17:00\",\"ss_is_blocked\":\"0\",\"created_at\":\"2021-08-03T14:17:40.000000Z\",\"updated_at\":\"2021-08-03T14:17:40.000000Z\"},{\"ss_id\":5.0,\"ss_device_id\":6.0,\"ss_title\":\"test test\",\"ss_day\":\"mon\",\"ss_start_time\":\"07:50:00\",\"ss_end_time\":\"08:50:00\",\"ss_is_blocked\":\"0\",\"created_at\":\"2021-08-03T14:22:51.000000Z\",\"updated_at\":\"2021-08-03T14:22:51.000000Z\"}]"
                    //  var data = getDataList(strJson)


                }
            }
        }
    }

    private fun checkvaluew(context: Context) {
       // taskArrayList = getArrayList("smartmodule", context)
      //  var taskArrayList: ArrayList<SmartschedulModelJava?>? = ArrayList()
        var taskArrayList: ArrayList<SmartschedulModel?>? = ArrayList()
         taskArrayList = getArrayList("smartmodule", context)

        if(taskArrayList != null) {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val dayOfWeek = mcurrentTime[Calendar.DAY_OF_WEEK]
            var daycheck = callday(dayOfWeek)
            for (item in taskArrayList!!) {
                if (item!!.ss_day == daycheck) {
                    val separated: List<String> = item!!.ss_start_time!!.split(":")
                    var h = separated[0]
                    var m = separated[1]
                    val eseparated: List<String> = item!!.ss_end_time!!.split(":")
                    var eh = eseparated[0]
                    var em = eseparated[1]

                    //int value = Integer.parseInt(string);
//                    if(m == "23"){
//                        h = "24";
//                    }
                    //int value = Integer.parseInt(string);
//                    if(m == "23"){
//                        h = "24";
//                    }
                    if (em == "59") {
                        eh = "24"
                    }

                    if (hour >= h.toInt()) {
                        if (eh.toInt() > hour) {
                             lockkeystate = true
                            /// preferenceManager!!.setscreenlock(false)
                               preferenceManager!!.setsmartschlock(false)
                             Toast.makeText(context,"no lock",Toast.LENGTH_LONG).show()
                             break
                         }
                    }
                }

            }
            if (!lockkeystate) {
                 lockmobile(context)
            }

        }else{
            preferenceManager!!.setsmartschlock(true)
            //preferenceManager!!.setscreenlock(false)
        }
    }

    private fun lockmobile(context: Context) {
        //preferenceManager!!.setscreenlock(true)
        preferenceManager!!.setsmartschlock(true)

        Toast.makeText(context, "LocK OFF", Toast.LENGTH_LONG).show()


//        val mDPM = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
//        val mDeviceAdminRcvr = ComponentName(context, DevAdminReceiver::class.java)
//        val active = mDPM.isAdminActive(mDeviceAdminRcvr)
//        if (active) {
//            mDPM.lockNow()
//        }
//
//        //Log.i("TAG", "checkvale: " + h)
//        val intent =
//            Intent(context, GestureSelfUnlockActivity::class.java)
//        intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, AppConstants.APP_PACKAGE_NAME)
//        intent.putExtra(
//            AppConstants.LOCK_FROM,
//            AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY
//        )
//        context!!.startActivity(intent)



    }

    private fun callday(dayOfWeek: Int): String {
        when(dayOfWeek){
            1 ->{ dayof ="Sun"
                return  dayof
            }
            2 -> {dayof = "Monday";
                return dayof
            }
            3 -> {dayof = "Tuesday"
                return dayof
            };
            4 ->  {dayof ="Wed"
                return dayof
            };
            5 -> {dayof = "Thursday"
                return dayof
            };
            6 -> {dayof = "Fri"
                return dayof
            };
            7 -> {dayof = "Sat"
                return dayof
            };

        }
        return dayof

    }



    fun getDataList(tag: String?): List<SmartschedulModel?>? {

        var datalist: List<SmartschedulModel> = ArrayList<SmartschedulModel>()
        val gson = Gson()
        datalist = gson.fromJson<List<SmartschedulModel>>(tag, object : TypeToken<List<SmartschedulModel?>?>() {}.type)
        return datalist
    }

    fun getArrayList(key: String?, context: Context): ArrayList<SmartschedulModel?>? {
        try {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val gson = Gson()
            val json = prefs.getString(key, null)
            val type = object : TypeToken<ArrayList<SmartschedulModel?>?>() {}.type
            return gson.fromJson<ArrayList<SmartschedulModel?>>(json, type)
        }catch (e :Exception){
            e.stackTrace
        }

        return null
    }




}