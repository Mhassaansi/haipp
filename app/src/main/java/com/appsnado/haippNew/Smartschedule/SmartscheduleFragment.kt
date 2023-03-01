package com.appsnado.haippNew.Smartschedule

import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.Comman_Pacakges.data.DbContract
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.SmartSchbind
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.retro.WebServiceConstants
import com.google.firebase.database.*
import org.json.JSONException
import org.json.JSONObject


class SmartscheduleFragment : BaseFragment<SmartscheduleViewModel>(), View.OnClickListener {
    var binding: SmartSchbind? = null
    var taskAdapter: SmartSchAdapter? = null
//    var taskArrayList: ArrayList<String>? = null
    var reponces: ArrayList<SmartschedulModel>? = null

    var Dialog: SetSmartScheduleDialog? = null
    var cal_services : Boolean =false

    var add_services : Boolean =false
    var notify_s :String = ""



    var myRefoneapp :DatabaseReference?=null
    var listener: ValueEventListener? =null

    companion object {
        @kotlin.jvm.JvmField
        var not_detail: SmartscheduleFragment? = null
        fun newInstance() = SmartscheduleFragment()

    }

    private lateinit var smartschviewModel: SmartscheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.smartschedule_fragment,
            container,
            false
        )
        startTopToBottomAnimation()
        setData("0")
        not_detail = this
        return binding!!.root;

    }

    private fun startTopToBottomAnimation() {
    }

    fun setData(s :String) {
        reponces= ArrayList()
        binding!!.btnAdd.setOnClickListener(this)

       // notify_s = s
        binding!!.rvTask.layoutManager = LinearLayoutManager(
            mainActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        taskAdapter = SmartSchAdapter(mainActivity!!, reponces!!)
        binding!!.rvTask.adapter = taskAdapter


         smartschviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
         smartschviewModel!!.userLiveData!!.observe(mainActivity!!, ReObserver())
         smartschviewModel!!.loadDataNetwork(mainActivity?.preferenceManager?.getdevicesid()!!,s)
         cal_services = true




//        binding!!.rvTask.layoutManager = LinearLayoutManager(
//            mainActivity,
//            LinearLayoutManager.VERTICAL,
//            false
//        )
//        taskAdapter =
  //      arrayOf<String?>("Select Days", "Monday", "Tuesday", "Wed", "Thursday", "Fri", "Sat", "Sun")

//            SmartSchAdapter(mainActivity!!, taskArrayList)
//        binding!!.rvTask.adapter = taskAdapter



    }


    override fun createViewModel(): SmartscheduleViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        smartschviewModel = ViewModelProviders.of(this, factory).get(SmartscheduleViewModel::class.java)
        return smartschviewModel
    }

    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(baseActivity!!, "Smart Schedule")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> {

                Dialog = SetSmartScheduleDialog(mainActivity!!, "Email")
                mainActivity!!.supportFragmentManager?.let {
                    Dialog!!.show(
                        it,
                        null
                    )
                }




//                taskArrayList!!.add("hello")
//                taskAdapter!!.notifyDataSetChanged()
            }
        }
    }


    fun call(name: String, day: String?, stime: String?, etime: String?) {

        try {






//        taskArrayList!!.add("hello")
//        taskAdapter!!.notifyDataSetChanged()
        smartschviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        smartschviewModel!!.ReuserLiveData!!.observe(mainActivity!!, AddObserver())
        smartschviewModel!!.addtask(mainActivity!!.preferenceManager?.getdevicesid(), name,day!!,stime!!,etime!!)
        add_services = true

           }catch (e : Exception){
            e.stackTrace
        }
    }



    fun firebasesetsmartsch(strJson: String) {
        val tvPN = ContactsContract.Directory.PACKAGE_NAME
        var usern = mainActivity?.preferenceManager?.getUser()?.userEmail
        val separated: List<String> = usern!!.split("@")
        var username = separated[0]
        separated[1]
        var deviceTitle = mainActivity?.preferenceManager?.getdevicestitle()
        var devicesid = mainActivity?.preferenceManager?.getdevicesid()
        val database = FirebaseDatabase.getInstance()
        val ui: Double = WebServiceConstants.userid!!.toDouble()
        val i = ui!!.toInt()
        val vi: Double = devicesid!!.toDouble()
        val v = vi!!.toInt()
        val myRef1 = database.getReference("Devicesdata")
        val myRefoneparent = myRef1.child("Parent_" + username + "_" + i.toString())
        val Childdevices = myRefoneparent.child("Childdevices")
        val myRefone = Childdevices.child("Child_" + deviceTitle + "_" + v.toString())
        val fierbasetoken = myRefone.child("smartsch");
        fierbasetoken.setValue(strJson)

//        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
//        val editor = prefs.edit()
//        val gson = Gson()
//        editor.putString("smartmodule", strJson)
//        editor.apply()


//         myRefoneapp!!.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Check for null
//                if (dataSnapshot.value == null) {
//                    Log.e("TAG", "User data is null!")
//
//                    return
//                }
//                DbContract.install = dataSnapshot.value.toString()
//            }
//            override fun onCancelled(error: DatabaseError) {
//                // Failed to read value
//                Log.e("TAG", "Failed to read user", error.toException())
//            }
//        })
    }


    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()
        }
    }


    private inner class ReObserver : Observer<java.util.ArrayList<SmartschedulModel>> {
        override fun onChanged(reponces: java.util.ArrayList<SmartschedulModel>?) {
            if(!cal_services) return
            if (reponces == null) return
               cal_services = false

                // saveArrayList(reponces, "smartmodule")


                taskAdapter = SmartSchAdapter(mainActivity!!, reponces!!)
                binding!!.rvTask.adapter = taskAdapter


        }
    }

//    fun saveArrayList(list: List<SmartschedulModel?>?, key: String?) {
//        val prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity)
//        val editor = prefs.edit()
//        val gson = Gson()
//        val json = gson.toJson(list)
//        editor.putString(key, json)
//         editor.apply()
//    }

    private inner class AddObserver : Observer<WebResponse<Any?>?> {
        override fun onChanged(reponces: WebResponse<Any?>?) {
            if(!add_services) return
            if (reponces == null) return
                 add_services = false
               ///  mainActivity?.toast(reponces.message!!,1)
                 setData("1")
                 NotificationSend("true","updatejson","")
                 navController!!.navigate(R.id.smartAppBlockerFragment)

        }
    }

    private inner class DeletObserver : Observer<WebResponse<Any?>?> {
        override fun onChanged(reponces: WebResponse<Any?>?) {
            if(!add_services) return
                if (reponces == null) return
                 add_services = false
                 setData("1")
                 NotificationSend("true","updatejson","")
        }
    }
     fun NotificationSend(ischeck: String, appnname: String,json: String) {
        var texts = ""
        val notification = JSONObject()
        val dataBody = JSONObject()
        val notificationBody = JSONObject()
        try {

            if(DbContract.childtoken != "") {
                texts = "Parent Update Your smart schedule"

                dataBody.put(appnname, json)
                dataBody.put("ischeck", ischeck)
                dataBody.put("bodymsg", texts)
                notificationBody.put("title", "Haipp")
                notificationBody.put("body", "Haipp")
                notificationBody.put("click_action", "Haipp")
                notificationBody.put("notification_type", "Haipp")
                notification.put("to", DbContract.childtoken)
                notification.put("priority", "high")
                notification.put("data", dataBody)
                notification.put("notification", notificationBody)
                mainActivity?.sendNotification(notification)
                mainActivity?.sendapi(mainActivity!!.preferenceManager!!.getdevicesid(),"Smart Schedule ",texts,"device")

            }else{
                Toast.makeText(mainActivity,"Please again select devices",Toast.LENGTH_LONG).show()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun deletdata(ssId: String?) {
        try {
            smartschviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
            smartschviewModel!!.DeleteLiveData!!.observe(mainActivity!!, DeletObserver())
            smartschviewModel!!.delet(ssId!!)
            add_services = true

        }catch (e : Exception){
            e.stackTrace
        }
    }
}