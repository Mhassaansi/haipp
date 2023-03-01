package com.appsnado.haippNew.AppBlockerActivity

import android.app.admin.DevicePolicyManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.content.pm.PackageManager.MATCH_DISABLED_COMPONENTS
import android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.appsnado.haippNew.Comman_Pacakges.data.DbContract
import com.appsnado.haippNew.DevAdminReceiver
import com.appsnado.haippNew.Firebase.Model
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.MySingleton
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.AppBlocker
import com.appsnado.haippNew.retro.WebServiceConstants.userid
import com.facebook.FacebookSdk.getApplicationContext
import com.google.firebase.database.*
import org.json.JSONException
import org.json.JSONObject
import kotlin.jvm.Throws


class AppBlockerFragment : BaseFragment<AppBlockerViewModel>() {

    var binding: AppBlocker? = null
    var appBlockerAdapter: AppBlockerAdapter? = null
    var appBlockerArrayList: ArrayList<String>? = null
    var myAppsToUpdate: ArrayList<AppInfo>? = null
    var PACKAGE_NAME: String? = null
    private var order: Map<String, ArrayList<Model>> = HashMap()
    var selectedItems: ArrayList<Model>? = null
    var finalAppdata: ArrayList<Model>? = null
    var myRefoneapp :DatabaseReference?=null
    var listener: ValueEventListener? =null
    private var childtokenref : DatabaseReference?=null

    companion object {
        const val TAG = "MainActivity"
        const val CODE_UNINSTALL_RESULT = 1235
        const val ACTION_UNINSTALL_RESULT = "eu.sisik.removehideaps.ACTION_UNINSTALL_RESULT"
        fun newInstance() = AppBlockerFragment()


        @kotlin.jvm.JvmField
        var not_detail: AppBlockerFragment? = null
    }

    private val adminComponentName: ComponentName by lazy {
        ComponentName(mainActivity!!, DevAdminReceiver::class.java)
    }

    private val devicePolicyManager: DevicePolicyManager by lazy {
        mainActivity!!.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    private val receiver = object: BroadcastReceiver() {

        override fun onReceive(p0: Context?, intent: Intent?) {

            if(intent?.action == ACTION_UNINSTALL_RESULT) {
                Log.d("TAG", "uninstall result: " + intent?.getStringExtra(PackageInstaller.EXTRA_PACKAGE_NAME) + "|status="
                        + intent?.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE))
            }
        }


    }


    private fun refreshPackageList() {
        val pi = mainActivity!!.getPackageManager().getInstalledPackages(MATCH_UNINSTALLED_PACKAGES or MATCH_DISABLED_COMPONENTS)
        val sortedPi = pi.sortedBy { i -> i.packageName }
        pi.forEach { packageInfo ->
            packageInfo.packageName

        }
        myAppsToUpdate!!.clear()


    //    myAppsToUpdate!!.addAll(sortedPi)

    }


    private lateinit var appblockerviewModel: AppBlockerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.app_blocker_fragment,
            container,
            false
        )

        PACKAGE_NAME = mainActivity!!.getPackageName();

        if (!devicePolicyManager.isDeviceOwnerApp(PACKAGE_NAME)) {
            Toast.makeText(mainActivity, "You need to make this app device owner first!", Toast.LENGTH_LONG).show()

        }

        binding!!.update.setOnClickListener {
            var texts = "Update All Apps"
            val notification = JSONObject()
            val dataBody = JSONObject()
            val notificationBody = JSONObject()
            try{

                if(DbContract.childtoken != "") {

                 dataBody.put("updateapp","updateapp")
                dataBody.put("ischeck","true")
                dataBody.put("bodymsg",texts)
                notificationBody.put("title", "Haipp")
                notificationBody.put("body", texts)
                notificationBody.put("click_action", "Haipp")
                notificationBody.put("notification_type", "Haipp")
                notification.put("to",  DbContract.childtoken)
                notification.put("priority", "high")
                notification.put("data", dataBody)
                notification.put("notification", notificationBody)
                mainActivity?.sendNotification(notification)



            }else{
            Toast.makeText(mainActivity,"Please again select devices",Toast.LENGTH_LONG).show()
        }

            } catch (e: JSONException) {
                e.printStackTrace()
            }


        }

       // initViews()



         //var list  =  getAppsToUpdate()
        setData()
        not_detail = this
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }


    private fun initViews() {

    }





    override fun createViewModel(): AppBlockerViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        appblockerviewModel =
            ViewModelProviders.of(this, factory).get(AppBlockerViewModel::class.java)
        return appblockerviewModel!!
    }


    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(baseActivity!!, "Allow Apps")
    }



    fun getAppsToUpdate(): ArrayList<AppInfo> {

        val pm: PackageManager = mainActivity!!.getPackageManager()
        val installedApps = pm.getInstalledApplications(MATCH_UNINSTALLED_PACKAGES or MATCH_DISABLED_COMPONENTS)
        myAppsToUpdate = ArrayList<AppInfo>()
        for (aInfo in installedApps) {
            if (aInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
                // System apps
            } else {
                // Users apps
                val appInfo = AppInfo()

                appInfo.setAppname(aInfo.loadLabel(pm).toString())
                appInfo.setPacakagename(aInfo.packageName)
                appInfo.setHide("0")

                appInfo.setLauncher(pm.getLaunchIntentForPackage(aInfo.packageName).toString())
                try {
                    val info = pm.getPackageInfo(aInfo.packageName, 0)
                   // appInfo.setVersionName(info.versionName.toString())
                   // appInfo.setVersionCode("" + info.versionCode)

                    appInfo.setIcon(info.applicationInfo.loadIcon(pm))
                    myAppsToUpdate!!.add(appInfo)
                } catch (e: PackageManager.NameNotFoundException) {
                    Log.e("ERROR", "we could not get the user's apps")
                }
            }
        }
        return myAppsToUpdate!!
    }
    fun setData() {

        val tvPN = PACKAGE_NAME
        var usern = mainActivity?.preferenceManager?.getUser()?.userEmail
        val separated: List<String> = usern!!.split("@")
        var username = separated[0]
         separated[1]
        var deviceTitle= mainActivity?.preferenceManager?.getdevicestitle()
        var devicesid =  mainActivity?.preferenceManager?.getdevicesid()
        val database = FirebaseDatabase.getInstance()



        val ui: Double = mainActivity?.preferenceManager!!.getUser()!!.userID!!.toDouble()
        val i = ui!!.toInt()
        val vi: Double = devicesid!!.toDouble()
        val v = vi!!.toInt()

        val myRef1 = database.getReference("Devicesdata")
        val myRefoneparent = myRef1.child("Parent_"+username+"_"+i.toString())
        val Childdevices = myRefoneparent.child("Childdevices")
        val myRefone = Childdevices.child("Child_"+deviceTitle +"_" +v.toString())
         myRefoneapp = myRefone.child("Appsdata");

        childtokenref = myRefone.child("token");
        childtokenref!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check for null
                if (dataSnapshot.value == null) {
                    Log.e("TAG", "User data is null!")
                    UIHelper.hidedailog()
                    return
                }
                DbContract.childtoken = dataSnapshot.value.toString()
                Log.e("TAG", "User data is null!")
                UIHelper.hidedailog()

            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                UIHelper.hidedailog()
                Log.e("TAG", "Failed to read user", error.toException())
            }
        })

       // val myRef = myRefoneapp.child("Apps");

        // DatabaseReference myRef = database.getReference("Apps");
        // DatabaseReference myRef = database.getReference("Apps");


        listener = myRefoneapp!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check for null
                if (dataSnapshot.value == null) {
                    Log.e(TAG, "User data is null!")

                    return
                }
                order = dataSnapshot.value as Map<String, ArrayList<Model>>
                selectedItems = ArrayList(order.get("Apps"))
                Log.i(TAG, "onDataChange: $selectedItems")
                finalAppdata = selectedItems

                var dataSnapshot1: DataSnapshot
                dataSnapshot.value





//                        for (i in 0 until selectedItems!!.size) {
//                         ///   val item: HashMap<Any,Any> = selectedItems!!.get(i)
//                            //val item: HashMap<Any,Any> = finalAppdata!!.get(i)
//                            for (info in commLockInfoslist) {
//
////                                        if (item.get("appname").toString().equals(list.get(j).getAppName())) {
////                                            final CommLockInfo lockInfo = list.get(j);
////                                            changeItemLockStatus("true", lockInfo, j);
////
////                                        }else if (item.get("packageName").toString().equals(list.get(j).getPackageName())) {
////                                            final CommLockInfo lockInfo = list.get(j);
////                                            changeItemLockStatus(item.get("isLocked").toString(), lockInfo, j);
////                                               break;
////                                        }
//                                if (info.packageName.equals(selectedItems!!.get(i).pacakagename)) {
//                                    val lockInfo: CommLockInfo = info
//                                    changeItemLockStatus(info.isLocked, lockInfo, j)
//                                    break
//                                }
//                            }
//                        }






                        binding!!.rvAppBlocker.setLayoutManager(
                    LinearLayoutManager(
                        mainActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                )
                appBlockerAdapter = AppBlockerAdapter(
                    mainActivity!!,
                        selectedItems
                )
                binding!!.rvAppBlocker.adapter = appBlockerAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException())
            }
        })




















//        appBlockerArrayList = ArrayList()
////        val pm: PackageManager = mainActivity!!.getPackageManager()
//////get a list of installed apps.
//////get a list of installed apps.
////        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
//
////        for (packageInfo in packages) {
////
////            appBlockerArrayList!!.add(packageInfo.packageName)
////
////
////
////           // Log.d(TAG, "Installed package :" + packageInfo.packageName)
////           // Log.d(TAG, "Source dir : " + packageInfo.sourceDir)
////          //  Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName))
////        }
//
//        appBlockerArrayList!!.add("Face Time")
//        appBlockerArrayList!!.add("Instagram")
//        appBlockerArrayList!!.add("LinkedIn")
//        appBlockerArrayList!!.add("Skype")
//        appBlockerArrayList!!.add("Messenger")
//        appBlockerArrayList!!.add("Snapchat")
//        appBlockerArrayList!!.add("Twitter")
//        appBlockerArrayList!!.add("WhatsApp")
//        appBlockerArrayList!!.add("Netflix")
//        appBlockerArrayList!!.add("LinkedIn")
//        appBlockerArrayList!!.add("Youtube")
//
//        binding!!.rvAppBlocker.setLayoutManager(
//            LinearLayoutManager(
//                mainActivity,
//                LinearLayoutManager.VERTICAL,
//                false
//            )
//        )
//        appBlockerAdapter = AppBlockerAdapter(
//            mainActivity!!,
//            appBlockerArrayList
//        )
//        binding!!.rvAppBlocker.adapter = appBlockerAdapter
    }

    fun send(position: Int, pacakagename: String?) {

           myAppsToUpdate!!.get(0).setHide("1")

        if (packageNameValid(pacakagename!!)) {
            val makeHidden = !devicePolicyManager.isApplicationHidden(adminComponentName, pacakagename)
            devicePolicyManager.setApplicationHidden(adminComponentName, pacakagename, makeHidden)
         //   butHide.text = if (makeHidden) "Unhide" else "Hide"
            if (makeHidden)
                myAppsToUpdate!!.get(0).setHide("0")
                else
                myAppsToUpdate!!.get(0).setHide("1")


            appBlockerAdapter?.notifyDataSetChanged()




//            if (packageNameValid(pn)) {
//                uninstallPackage(pn)
//            } else {
//                Toast.makeText(this, "Select a valid package name first", Toast.LENGTH_SHORT).show()
//            }
                  //refreshPackageList()
        } else {
            Toast.makeText(mainActivity, "Select a valid package name first", Toast.LENGTH_SHORT).show()
        }




        // MainActivity.not_detail!!.applauncher(pacakagename)


    }


//    private fun uninstallPackage(packageName: String) {
//        val intentSender = PendingIntent.getBroadcast(this,
//            CODE_UNINSTALL_RESULT,
//            Intent(ACTION_UNINSTALL_RESULT),
//            0).intentSender
//
//        val pi = packageManager.packageInstaller
//        pi.uninstall(packageName, intentSender)
//    }

    private fun packageNameValid(packageName: String): Boolean {
        try {
            mainActivity!!.packageManager.getPackageInfo(packageName, MATCH_UNINSTALLED_PACKAGES or MATCH_DISABLED_COMPONENTS)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            // ignore
        }

        return false
    }

    override fun onDestroy() {
        super.onDestroy()
         myRefoneapp?.removeEventListener(listener!!)

    }

    fun sendmessage(position: Int, toString: String, items: ArrayList<Model>, ischeck: String, pacakagename: String,appname :String) {

        UIHelper.showDilog(mainActivity)



        val map = java.util.HashMap<String, java.util.ArrayList<Model>>()
        /// myRef.removeValue()
        /// myRef.removeValue()
         Log.d(TAG, "send: $myRefoneapp")
          map.put("Apps", items)

        /// map["appdata"] = values



        ////  mAdapter.notifyDataSetChanged();
        ////  mAdapter.notifyDataSetChanged();
        try {
            myRefoneapp?.setValue(map)
            NotificationSend(ischeck,pacakagename,appname)
        } catch (e: Exception) {
            e.stackTrace
        }


        val handler = Handler()
        handler.postDelayed({

            UIHelper.hidedailog()

            // Do something after 5s = 5000ms
        }, 6000)

    }

    private fun NotificationSend(ischeck: String, pacakagename :String,appnname: String) {
        var texts = ""
        val notification = JSONObject()
        val dataBody = JSONObject()
        val notificationBody = JSONObject()
        try {
            if(ischeck.equals("true")){
                 texts = appnname + " is Lock"
            }else{
                 texts = appnname + " is Unlock"
            }

            if(DbContract.childtoken != "") {
            dataBody.put("packagename",pacakagename)
            dataBody.put("ischeck",ischeck)
            dataBody.put("bodymsg",texts)
            notificationBody.put("title", "Haipp")
            notificationBody.put("body", texts)
            notificationBody.put("click_action", "Haipp")
            notificationBody.put("notification_type", "Haipp")
            notification.put("to",  DbContract.childtoken)
              //  notification.put("to","d46LEasXRBG6nzQoEd209a:APA91bFTX24crjJvLqiiYOT5_cP3eOwGmssct2uIeguSH6NG19fTLUfJuHn0cKzFS1a6GWtcJhkCb2JUZ14A4vim9LUr4dGc2XrKHRx1IF5UcmBXMIMhInaT6RWAtyllsaNciATebxlC")

            notification.put("priority", "high")
            notification.put("data", dataBody)
            notification.put("notification", notificationBody)
            mainActivity?.sendNotification(notification)

        }else{
            Toast.makeText(mainActivity,"Please again select devices",Toast.LENGTH_LONG).show()
        }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


}

