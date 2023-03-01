package com.appsnado.haippNew.Adddevices

import android.app.ProgressDialog
import android.app.admin.DevicePolicyManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.Adddevices.AdddevicesFragment.Companion.mUsageStatsManager
import com.appsnado.haippNew.Applocakpacakges.base.AppConstants.commLockInfoslist
import com.appsnado.haippNew.Applocakpacakges.base.AppConstants.firebaseappstatus
import com.appsnado.haippNew.Applocakpacakges.db.CommLockInfoManager
import com.appsnado.haippNew.Applocakpacakges.model.CommLockInfo
import com.appsnado.haippNew.Applocakpacakges.mvp.p.LockMainPresenter
import com.appsnado.haippNew.Applocakpacakges.widget.DialogSearch
import com.appsnado.haippNew.DevAdminReceiver
import com.appsnado.haippNew.Firebase.AppInfo
import com.appsnado.haippNew.Firebase.CustomUsageStats
import com.appsnado.haippNew.Firebase.Model
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory

import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.AdddevicesChild
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.retro.WebServiceConstants.devices_id
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.apache.http.ParseException
import org.litepal.crud.DataSupport
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import android.os.AsyncTask
import com.appsnado.haippNew.Activities.MainActivity
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.Activities.MainActivity.Companion.tokenfirebasedata
import com.appsnado.haippNew.R


class AdddevicesFragment :  BaseFragment<Adddevicesviewmodel>(), View.OnClickListener {
    var binding: AdddevicesChild? = null
    var kidAdapter: AdddevicesAdapter? = null
    var kidsArrayList: ArrayList<String>? = null
    var Dailog: adddevicesdailog?= null
    var cal_services : Boolean =false
    var add_services : Boolean =false
    private val imgview: ImageView? = null
    private var mLockMainPreseter: LockMainPresenter? = null
    private var mDialogSearch: DialogSearch? = null
    private var mLockInfoManager: CommLockInfoManager? = null
    var count = 0
    var bitmaps:Bitmap? = null
    var map = HashMap<String, ArrayList<Model>>()
    var myAppsToUpdate: ArrayList<AppInfo>? = null
    var appdata: ArrayList<Model>? = null
    var finalAppdata: ArrayList<Model>? = null
    var databasearray: ArrayList<Model>? = null
    var usageStatsList: List<UsageStats>? = null
    var sortdatausage: ArrayList<AppInfo>? = null

    var mHandler: Handler? = null
    private var mDPM: DevicePolicyManager? = null
    private val pgsBar: ProgressBar? = null
    private var pi = 0
    private val hdlr = Handler()

    private var mDeviceAdminRcvr: ComponentName? = null
    companion object {
        @kotlin.jvm.JvmField
        var not_detail: AdddevicesFragment? = null
        fun newInstance() = AdddevicesFragment()
        var mUsageStatsManager: UsageStatsManager? = null
    }

    private lateinit var viewModelkids: Adddevicesviewmodel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                com.appsnado.haippNew.R.layout.adddevices_fragment,
                container,
                false
        )

        mainActivity?.getWindow()?.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        // startTopToBottomAnimation()


       mLockInfoManager = CommLockInfoManager(mainActivity)
//        mLockMainPresenter = LockMainPresenter(this, mainActivity)
//        mLockMainPresenter?.loadAppInfo(mainActivity)

        mDPM = mainActivity?.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        mDeviceAdminRcvr = ComponentName(mainActivity!!, DevAdminReceiver::class.java)
        val active = mDPM!!.isAdminActive(mDeviceAdminRcvr!!)
        if (active) {
            setData()

        } else {
            activate()
        }








        binding!!.adddevices.setOnClickListener(this)
        not_detail = this
        return binding!!.root;

    }


    fun changeItemLockStatus(boleankey: String, pacakegename: String?) {
        try {
            mLockInfoManager!!.lockCommApplication("com.android.settings")
        } catch (e: java.lang.Exception) {
            e.cause
        }
        //notifyItemChanged(position);
    }

    fun activate() {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                mDeviceAdminRcvr)
        startActivityForResult(intent, 122)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 122) {
             setData()
        }
    }


    private fun startTopToBottomAnimation() {
//        val animation = AnimationUtils.loadAnimation(mainActivity, com.appsnado.haipp.R.anim.slide_in_from_top)
//        binding?.linearLayoutCompat2!!.startAnimation(animation)

    }



    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()
        }
    }
    private inner class AddObserver : Observer<WebResponse<Any?>?> {
        override fun onChanged(reponces: WebResponse<Any?>?) {
            if(!add_services) return
            if (reponces == null) return
            add_services = false
            mainActivity?.toast(reponces.message!!,1)
            setData()

        }
    }
    private inner class ReObserver : Observer<java.util.ArrayList<Adddevicesmodel>> {
        override fun onChanged(reponces: java.util.ArrayList<Adddevicesmodel>?) {
            if(!cal_services) return
            if (reponces == null) return
                cal_services = false

                binding!!.rvFeatures.setLayoutManager(
                        LinearLayoutManager(
                                mainActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                        )
                )
                kidAdapter = AdddevicesAdapter(mainActivity!!, reponces)
                binding!!.rvFeatures.adapter = kidAdapter

        }
    }


    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)
    }

    private fun dataparse(C_date: String):Long {
        // val miliSecsDate: Long = milliseconds("2015-06-04")
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        try {
            val mDate: Date = sdf.parse(C_date)
            val timeInMilliseconds = mDate.time
            println("Date in milli :: $timeInMilliseconds")
            return timeInMilliseconds
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return  0

    }

    fun setData() {

        var currentTime = System.currentTimeMillis()

        var C_date = convertLongToTime(currentTime)
        var date = dataparse(C_date)
        Log.i("TAG", "setData: " + date)

        AsyncTask.execute {
            mUsageStatsManager = mainActivity?.getSystemService("usagestats") as UsageStatsManager
            val statsUsageInterval: StatsUsageInterval? = StatsUsageInterval
                .getValue("Daily")

//        val queryUsageStats = mUsageStatsManager!!
//                .queryUsageStats(intervalType, cal.getTimeInMillis(),
//                        System.currentTimeMillis())

            var da: Long = System.currentTimeMillis() - 86400000
            if (statsUsageInterval != null) {
                usageStatsList = getUsageStatistics(statsUsageInterval.mInterval, date)
                Collections.sort(usageStatsList, LastTimeLaunchedComparatorDesc(date))
                updateAppsList(usageStatsList!!, mainActivity!!)

                //sortdatausage = Array();
                sortdatausage = ArrayList<AppInfo>()
                for (info in usageStatsList!!) {
                    if (info.lastTimeStamp > date) {
                        val appInfo = AppInfo()
                        appInfo.toltaltime = info.totalTimeInForeground.toString()
                        appInfo.pacakagename = info.packageName.toString()
                        sortdatausage!!.add(appInfo)
                    }
                }
            }
        }
                viewModelkids!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
                viewModelkids!!.userLiveData!!.observe(mainActivity!!, ReObserver())
                viewModelkids!!.loadDataNetwork(mainActivity!!.preferenceManager!!.getUser()!!.userID!!)
                cal_services = true





    }




        override fun createViewModel(): Adddevicesviewmodel? {
            val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
            viewModelkids = ViewModelProviders.of(this, factory).get(Adddevicesviewmodel::class.java)
            return viewModelkids!!
        }

        override fun setTitleBar(titleBar: TitleBar?) {
            titleBar?.visibility = View.VISIBLE
            titleBar?.txtTitleName?.text = "Add Kids Devices"
            titleBar?.btnLeft?.visibility = View.VISIBLE
            titleBar?.setBtnLeft(R.drawable.backbutton, View.OnClickListener {
                MainActivity.navController!!.popBackStack()
            });

        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.adddevices -> {
                    Dailog = adddevicesdailog(mainActivity!!, "Email")
                    mainActivity!!.supportFragmentManager?.let {
                        Dailog!!.show(
                                it,
                                null
                            )
                    }
                }

            }
        }



        fun getallApps(
            commLockInfoslist: MutableList<CommLockInfo>,
            devicesid: String?,
            userid: String?,
            username: String,
            deviceTitle: String?
        ): List<CommLockInfo>? {
            val progressDialog = ProgressDialog(mainActivity)
            progressDialog.setTitle("Uploading...")
            progressDialog.setTitle("Please Wait")
            progressDialog.setCancelable(false)
            progressDialog.show()
            val commLockInfos = DataSupport.findAll(CommLockInfo::class.java)
            val database = FirebaseDatabase.getInstance()
            val pm: PackageManager = mainActivity!!.getPackageManager()
            myAppsToUpdate = ArrayList<AppInfo>()
            appdata = ArrayList<Model>()
        //    pi = pgsBar?.getProgress()!!;

          //  Thread(Runnable {


                for (info in commLockInfoslist) {
                    val appInfo = AppInfo()
                    appInfo.setAppname(info.appName)
                    appInfo.setPacakagename(info.packageName)
                    appInfo.setHide("0")

                    /// val appInfoicon: ApplicationInfo = info.getAppInfo()
                    var apps: HashMap<*, *> = Model()

                    apps["pacakagename"] == info.appName

                    if (info.getAppName().equals("Settings")) {
                        apps["isLocked"] == "false"
                    } else {
                        apps["isLocked"] == "false"
                    }
                      apps["appname"] == info.getAppName()
                       var appin: PackageInfo? = null
                    try {
                        appin = pm.getPackageInfo(info.packageName!!, 0)
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                    }

                   // mLockInfoManager!!.lockCommApplication(info.packageName!!)

                    try {

                        appInfo.icon = appin?.applicationInfo?.loadIcon(pm)
                        val drawable = appin?.applicationInfo?.loadIcon(pm)
                        binding?.img?.setImageDrawable(appin?.applicationInfo?.loadIcon(pm))
                        if(drawable != null)
                            bitmaps = drawableToBitmap(drawable!!)
                        myAppsToUpdate?.add(appInfo)

                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                    }

                    // appInfo.setVersionName(info.versionName.toString())
                    // appInfo.setVersionCode("" + info.versionCode)



                    //  finalAppdata.add((Model) apps);


                    ///   if (count == arrImagesPath!!.size) {
                    try {

//
//                var downloadUrl: Task<Uri>? = null
//                val baos = ByteArrayOutputStream()
//                bitmaps.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//                val data: ByteArray = baos.toByteArray()
//                val storage: FirebaseStorage = FirebaseStorage.getInstance()
//                val storageRef: StorageReference =
//                        storage.getReferenceFromUrl("https://apptest-c1699-default-rtdb.firebaseio.com/")
//                val imagesRef: StorageReference = storageRef.child(
//                        "images/" + aInfo.loadLabel(
//                                pm
//                        ).toString() + ".jpg"
//                )
//                val uploadTask: UploadTask = imagesRef.putBytes(data)
                        val storage = FirebaseStorage.getInstance()
                        val baos = ByteArrayOutputStream()
                        bitmaps!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()

//                                .jpg
                        //    val storageRef = storage.getReferenceFromUrl("gs://demotestapp-8623a.appspot.com")

                     ///   "https://firebasestorage.googleapis.com/v0/b/haipp-6a7d3.appspot.com/o/images%2Fcom.android.vending.png?alt=media&token=da3fa711-e011-4630-8808-010a3cbc4d02"
                        val storageRef = storage.getReferenceFromUrl("gs://haipp-6a7d3.appspot.com")
                        val imagesRef = storageRef.child("images/" + info.packageName.toString().toString() + ".png")
                        val uploadTask = imagesRef.putBytes(data)
                        val finalMyAppsToUpdate: ArrayList<AppInfo> = myAppsToUpdate!!
                        finalAppdata = appdata
                        uploadTask.addOnSuccessListener { taskSnapshot ->
                            //    Commonvarible.globalObj.Dialogsetjob(getActivity(), " Uploaded","");
                            //    Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                            //    downloadUrl = taskSnapshot.getDownloadUrl();
                            ///   sendMsg("" + downloadUrl, 2);
                            /*   downloadUrl= taskSnapshot.getUploadSessionUri();
                                                                 Log.d("downloadUrl-->", "" + downloadUrl);*/
                            val downloadUrlss = taskSnapshot.storage.downloadUrl
                            val uri = taskSnapshot.storage.downloadUrl
                            while (!uri.isComplete);
                            val url = uri.result
                            Log.i(ContentValues.TAG, "onSuccess: $downloadUrlss")
                            Log.i(ContentValues.TAG, "onSuccess: $url")
                            Log.d("downloadUrl-->", "" + downloadUrlss)
                            count++
                            apps["icon"] == url.toString()

                                    val separated: List<String> = url.toString()!!.split("images%2F")
                                    var oneurl = separated[0]
                                    var twourl = separated[1]

                                 for(element in myAppsToUpdate!!) {
                                  if (twourl.startsWith(element.pacakagename)) {


                                       val applist = Model()


//                                         applist.set(
//                                             "pacakagename",
//                                              myAppsToUpdate!!.get(count - 1).pacakagename
//                                            )


                                           applist.set(
                                             "pacakagename",
                                               element.pacakagename
                                            )

                                            applist.set("hide", "0")
                                            applist.set("isLocked", false)

                                        //   applist.set("appname", myAppsToUpdate!!.get(count - 1).appname)
                                            applist.set("appname", element.appname)
                                            applist.set("unistall", "0")
                                            applist.set("icon", url.toString())

//                                applist["pacakagename"] = myAppsToUpdate!!.get(count-1).pacakagename
//                                applist["isLocked"] =  false
//                                applist["appname"] =  myAppsToUpdate!!.get(count-1).appname
//                                applist["icon"] = url.toString()



                                    finalAppdata?.add(applist)
                                    break
                                }

                            }

                            if (count == finalMyAppsToUpdate.size) {

                                Log.i("TAG", "onSuccess: ")
                                Log.d("TAG", "setApplication: ")
                                map.put("Apps", finalAppdata!!)


                                val ui: Double = userid!!.toDouble()
                                val i = ui!!.toInt()
                                val vi: Double = devicesid!!.toDouble()
                                val v = vi!!.toInt()

                                val myRef1 = database.getReference("Devicesdata")
                                val myRefoneparent = myRef1.child("Parent_"+username+"_"+i.toString())
                                val Childdevices = myRefoneparent.child("Childdevices")
                                val myRefone = Childdevices.child("Child_"+deviceTitle +"_" +v.toString())
                                val myRefoneapp = myRefone.child("Appsdata");
                                //val myReftwo = myRefone.child("Child_id  "+ v.toString())
                                myRefoneapp.setValue(map)



//                                val Apps_usage = myRefone.child("Apps_usage");
//                                myRefoneapp.setValue(map)




                                val myinstall = myRefone.child("installpermission");
                                myinstall.setValue(true)

                                val mobile = myRefone.child("Phonelock");
                                mobile.setValue(false)

                                val ChildLogout = myRefone.child("ChildLogout");
                                ChildLogout.setValue(false)
                                mainActivity?.preferenceManager?.setchildlogout(false)

                                val fierbasetoken = myRefone.child("token");
                                fierbasetoken.setValue(tokenfirebasedata)

                                mainActivity!!.preferenceManager!!.setlockscreen(false)

                                mainActivity!!.preferenceManager!!.setAppupdate(true)
                                firebaseappstatus = false
                                navController.popBackStack(R.id.roleFragment, true);
                                navController.popBackStack(R.id.adddevicesFragment, true);
                                navController!!.navigate(R.id.homeFragment)


                                ///usage_app(i,v,myRefone)
                                progressDialog!!.dismiss()
                                try {
                                    //myRef.setValue(map);
                                } catch (e: Exception) {
                                    progressDialog.dismiss()
                                    e.stackTrace
                                }
                            }
                        }




                                .addOnFailureListener { progressDialog.dismiss() }
                                .addOnProgressListener { taskSnapshot ->
                                 //   val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                                       //     .totalByteCount
                                   // progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                                }


//                        pi += 1
//                        // Update the progress bar and display the current value in text view
//                        hdlr.post(Runnable {
//                            pgsBar.setProgress(pi)
//                            //txtView.setText(pi.toString() + "/" + pgsBar.getMax())
//
//
//
//
//                        })
                    } catch (e: Exception) {
                        e.stackTrace
                    }


                }



//                        try {
//                            // Sleep for 100 milliseconds to show the progress slowly.
//                            Thread.sleep(100)
//                        } catch (e: InterruptedException) {
//                            e.printStackTrace()
//                        }
//
//                }).start()






////







            return commLockInfos
        }

        fun add(toString: String) {
            viewModelkids!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
            viewModelkids!!.ReuserLiveData!!.observe(mainActivity!!, AddObserver())
            viewModelkids!!.resend(mainActivity!!.preferenceManager!!.getUser()!!.userID!!,devices_id!!, toString)
            add_services = true
        }

        fun firebase(devicesid: String?, deviceTitle: String?) {


            val pm = mainActivity!!.getPackageManager()

            var usern = mainActivity?.preferenceManager?.getUser()?.userEmail
            val separated: List<String> = usern!!.split("@")
            var username = separated[0]
            separated[1]


            try {

                val info: PackageInfo = pm.getPackageInfo("acr.browser.barebones", PackageManager.GET_META_DATA)
                val intent = Intent()
                intent.setClassName("acr.browser.barebones", "com.appsnado.haippNew.MyBroadcastReceive")
                intent.action = "com.appsnado.haippNew.MyBroadcastReceive"
                intent.putExtra("userid", mainActivity?.preferenceManager?.getUser()?.userID)
                intent.putExtra("deviceid", devicesid)
                intent.putExtra("username", username)
                intent.putExtra("devicename", deviceTitle)
                mainActivity!!.sendBroadcast(intent)


                calldata(commLockInfoslist,devicesid,mainActivity?.preferenceManager?.getUser()?.userID,username,deviceTitle)

                mainActivity?.preferenceManager?.setsmartschlock(true)

            } catch (e: PackageManager.NameNotFoundException) {
                mainActivity!!.toast("Please First Install Haippbrowser",0)
                e.stackTrace

            }

        }





    private fun calldata(
            list: List<CommLockInfo>,
            devicesid: String?,
            userid: String?,
            username: String,
            deviceTitle: String?
        ) {

            val pm: PackageManager = mainActivity!!.getPackageManager()
            Log.i("TAG", "loadAppInfoSuccess: $list")
            val database = FirebaseDatabase.getInstance()
//            val myRef1 = database.getReference("Apps")
//            val myRefone = myRef1.child("3"!!)
//            val myReftwo = myRefone.child("1"!!)
            val commLockInfos = getallApps(commLockInfoslist,devicesid,userid,username,deviceTitle)
            Log.i("TAG", "calldata: "+commLockInfos)

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private fun usage_app(i: Int, v: Int, myReftwodata: DatabaseReference) {

            val database = FirebaseDatabase.getInstance()
            val myRef1 = database.getReference("Apps_usage")
            val map: HashMap<String, ArrayList<Model>> = HashMap<String, ArrayList<Model>>()
            databasearray = ArrayList<Model>()
            for (i in finalAppdata!!.indices) {
                val item: HashMap<Any,Any> = finalAppdata!!.get(i)
                for (j in 0 until sortdatausage?.size!!) {

                    if (item.get("pacakagename").toString()== sortdatausage?.get(j)?.pacakagename) {
                        val apps = Model()





//                        apps.set("lasttimeused", usageStatsList!!.get(j).getLastTimeUsed())
//                        apps.set("endtime",  usageStatsList!!.get(j).getLastTimeStamp())
//                        apps.set("begintime", usageStatsList!!.get(j).getFirstTimeStamp())

                        apps.set("totaltime", sortdatausage!!.get(j).toltaltime)
                        apps.set("pacakagename",  item.get("pacakagename").toString())
                        apps.set("isLocked", item.get("isLocked").toString())
                        apps.set("icon", item.get("icon").toString())
                        apps.set("appname",item.get("appname").toString())
                        apps.set("parent_id",i)
                        apps.set("Child_id",v)





                        databasearray!!.add(apps)
                        break
                    }
                    //                }else {
//                    HashMap apps = new Model();
//                    apps.put("packageName", item.get("packageName").toString());
//                    apps.put("isLocked",  item.get("isLocked").toString());
//                    //apps.put("icon",  item.get("icon").toString());
//                    databasearray.add((Model) apps);
//                }
                }
            }
//            map["usage_app"] == databasearray
            map.put("usage_app", databasearray!!)



            Log.i(ContentValues.TAG, "usage_app: $databasearray")

//            val myRefone = myRef1.child("Parent_id  "+ i.toString())
//            val myReftwo = myRefone.child("Child_id  "+ v.toString())
//            myReftwo.setValue(map)





        }

        fun drawableToBitmap(drawable: Drawable): Bitmap? {
            try {
                var bitmap: Bitmap? = null
            if (drawable is BitmapDrawable) {
                val bitmapDrawable = drawable
                if (bitmapDrawable.bitmap != null) {
                    return bitmapDrawable.bitmap
                }
            }
            bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
            } else {
                Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap

            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return null
        }



}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun updateAppsList(usageStatsList: List<UsageStats?>, mainActivity: MainActivity) {
        val customUsageStatsList: MutableList<CustomUsageStats> = ArrayList<CustomUsageStats>()
        for (i in usageStatsList.indices) {
            val customUsageStats = CustomUsageStats()
            customUsageStats.usageStats = usageStatsList[i]
            try {
                val appIcon: Drawable = mainActivity.getPackageManager()
                        .getApplicationIcon(customUsageStats.usageStats.getPackageName())
                  customUsageStats.appIcon = appIcon
            } catch (e: PackageManager.NameNotFoundException) {
                Log.w("TAG", java.lang.String.format("App Icon is not found for %s",
                        customUsageStats.usageStats.getPackageName()))
                customUsageStats.appIcon = mainActivity.getDrawable(R.drawable.ic_launcher)
            }
            customUsageStatsList.add(customUsageStats)
        }
//        mUsageListAdapter.setCustomUsageStatsList(customUsageStatsList);
//        mUsageListAdapter.notifyDataSetChanged();
//        mRecyclerView.scrollToPosition(0);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun getUsageStatistics(intervalType: Int, date: Long): List<UsageStats>? {
        // Get the app statistics since one year ago from the current time.

//        val cal = Calendar.getInstance()
//        val df = SimpleDateFormat("yyyy-MM-dd") // HH:mm:ss");
//        val reg_date: String = df.format(cal.time)
//        //showtoast("Currrent Date Time : $reg_date")
//        cal.add(Calendar.YEAR, -1) // number of days to add


        //86,400,000


        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -1)
        val queryUsageStats = mUsageStatsManager!!
                .queryUsageStats(intervalType, cal.timeInMillis,
                        System.currentTimeMillis())
        if (queryUsageStats.size == 0) {
            Log.i("TAG", "The user may not allow the access to apps usage. ")
//            mOpenUsageSettingButton.setVisibility(View.VISIBLE)
//            mOpenUsageSettingButton.setOnClickListener(View.OnClickListener {
//                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
//            })
        }
        return queryUsageStats
    }

internal enum class StatsUsageInterval(private val mStringRepresentation: String,  val mInterval: Int) {
    DAILY("Daily", UsageStatsManager.INTERVAL_DAILY), WEEKLY("Weekly", UsageStatsManager.INTERVAL_WEEKLY), MONTHLY("Monthly", UsageStatsManager.INTERVAL_MONTHLY), YEARLY("Yearly", UsageStatsManager.INTERVAL_YEARLY);

    companion object {
        fun getValue(stringRepresentation: String): StatsUsageInterval? {
            for (statsUsageInterval in StatsUsageInterval.values()) {
                if (statsUsageInterval.mStringRepresentation == stringRepresentation) {
                    return statsUsageInterval
                }
            }
            return null
        }
    }

}


private class LastTimeLaunchedComparatorDesc(date: Long) : Comparator<UsageStats?> {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun compare(o1: UsageStats?, o2: UsageStats?): Int {
        return java.lang.Long.compare(o2!!.lastTimeUsed, o2.lastTimeUsed)
    }
}