 package com.appsnado.haippNew

import android.Manifest
import android.annotation.SuppressLint
import android.app.admin.DevicePolicyManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.os.*
import android.preference.PreferenceManager
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.appsnado.haippNew.Activities.GeocoderHelperClass
import com.appsnado.haippNew.Activities.MainActivity
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.Activities.MainActivity.Companion.tokenfirebasedata
import com.appsnado.haippNew.Adddevices.Adddevicesmodel
import com.appsnado.haippNew.AppBlockerActivity.AppBlockerFragment
import com.appsnado.haippNew.Applocakpacakges.base.AppConstants.firebaseappstatus
import com.appsnado.haippNew.Applocakpacakges.db.CommLockInfoManager
import com.appsnado.haippNew.Applocakpacakges.model.CommLockInfo
import com.appsnado.haippNew.Applocakpacakges.mvp.contract.LockMainContract
import com.appsnado.haippNew.Applocakpacakges.mvp.p.LockMainPresenter
import com.appsnado.haippNew.Applocakpacakges.services.BackgroundManager
import com.appsnado.haippNew.Applocakpacakges.services.LockService
import com.appsnado.haippNew.Applocakpacakges.services.TimerCounter
import com.appsnado.haippNew.Applocakpacakges.services.lanListener
import com.appsnado.haippNew.Chat.FirebasetimeClass
import com.appsnado.haippNew.Chat.Messangermodel
import com.appsnado.haippNew.Chat.Timesortmessanger
import com.appsnado.haippNew.Comman_Pacakges.adapters.CustomArrayAdapter
import com.appsnado.haippNew.Comman_Pacakges.data.DbContract
import com.appsnado.haippNew.Comman_Pacakges.data.DbContract.Companion.devices
import com.appsnado.haippNew.Comman_Pacakges.data.DbContract.Companion.newsItems
import com.appsnado.haippNew.Firebase.AppInfo
import com.appsnado.haippNew.Firebase.CustomUsageStats
import com.appsnado.haippNew.Firebase.Model
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Home.LastModel
import com.appsnado.haippNew.Main.Bottomview
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory

import com.appsnado.haippNew.Monitorapp.data.AppItem
import com.appsnado.haippNew.Monitorapp.data.DataManager
import com.appsnado.haippNew.Screenlock.MyBroadCastReciever
import com.appsnado.haippNew.Smartschedule.SmartschedulModel
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.data.SharedPreferenceManager
import com.appsnado.haippNew.databinding.HomeBinding
import com.google.firebase.firestore.EventListener

import com.appsnado.haippNew.retro.WebServiceConstants.userid
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.messenger.MessengerUtils.PACKAGE_NAME
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.home_fragment.*
import org.apache.http.ParseException
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

 class HomeFragment : BaseFragment<HomeViewModel>(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks, OnCameraChangeListener,
    GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener,
    View.OnClickListener,
    LocationListener, lanListener, LockMainContract.View {
    var binding: HomeBinding? = null
    var mapView: MapView? = null
    var map: GoogleMap? = null
    var btlayout: Bottomview? = null
    var homeviewModel: HomeViewModel? = null
    var selectAdapter: ArrayAdapter<*>? = null
    var arraySpinner = arrayOf<String?>("Select Location", "Sidney", "US", "Melbourne")
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mCameraHandler: Handler? = null
    var checkid = ""
    private var childtokenref : DatabaseReference?=null
    private var tokenlistener: ValueEventListener? =null
    var countDownTimer : CountDownTimer? = null
    var spinnerCheck = 0
    private val counter = 0
    var tc: TimerCounter? = null
    var bitmaps: Bitmap? = null

    var cal_services: Boolean = false
    var spinneradapter: CustomArrayAdapter? = null
    var location_services: Boolean = false
    private var mDPM: DevicePolicyManager? = null

    private var usageStatsList: List<UsageStats>? = null
    private var sortdatausage: java.util.ArrayList<AppInfo>? = null
    private var mDeviceAdminRcvr: ComponentName? = null
    var mUsageStatsManager: UsageStatsManager? = null

    private var myRefoneapp: DatabaseReference? = null
    private var listener: ValueEventListener? = null
    private var listenerdata: ValueEventListener? = null

    private var Allappsref: DatabaseReference? = null
    private var applistenerdata: ValueEventListener? = null


    private var order: Map<String, ArrayList<Model>> = HashMap()
    private var selectedItems: ArrayList<Model>? = null
    private var finalAppdata: ArrayList<Model>? = null

    private var newinstallerapp: ArrayList<Model>? = null

    var myAppsToUpdate: java.util.ArrayList<AppInfo>? = null

    private var mLockInfoManager: CommLockInfoManager? = null
    private var mLockMainPresenter: LockMainPresenter? = null

    private var appsinstallercheck: Boolean? = false
    private var count = 0

    private var childclaim: DatabaseReference? = null
    private var minutes_firebase :Long = 0
    private var childclaimdata: DatabaseReference? = null

    private var listdata: List<CommLockInfo> = ArrayList()
    private var database: FirebaseFirestore? = null
    private var database2: FirebaseFirestore? = null

    val mArrayList = java.util.ArrayList<Messangermodel>()
    private val mArrayList_count =
        java.util.ArrayList<FirebasetimeClass?>()
    private val mArrayList_unread =
        java.util.ArrayList<FirebasetimeClass?>()

    var documentsnap : DocumentSnapshot? = null
    private var flgcall : Boolean? = false

    //    var spinnerCheck = 0
     var id :Double = 0.0
    var userId :Int = 0

    var greaterid: String? = null
    var smallerid: String? = null
    @JvmField
    var preferenceManager: SharedPreferenceManager? = null

    companion object {
        var homeselectchild :Boolean = false
        fun newInstance() = HomeFragment()

        @kotlin.jvm.JvmField
        var not_det: HomeFragment? = null
    }


    override fun createViewModel(): HomeViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        homeviewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
        return homeviewModel!!
    }

    fun saveArrayList(
            list: ArrayList<Model>?,
            key: String?
    ) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                com.appsnado.haippNew.R.layout.home_fragment,
                container,
                false
        )
        homeselectchild = false
        mGoogleApiClient = GoogleApiClient.Builder(mainActivity!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        spinnerCheck = 0
        binding!!.mapView.onCreate(savedInstanceState);
        binding!!.mapView.getMapAsync(this);
        mCameraHandler = Handler()

        mainActivity?.updateview(true)

        startTopToBottomAnimation();

        binding!!.actreport.setOnClickListener(this)
        binding!!.smartsch.setOnClickListener(this)
        binding!!.task.setOnClickListener(this)
        binding!!.worksheet.setOnClickListener(this)


        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10 * 1000.toLong()) // 10 seconds, in milliseconds
            .setFastestInterval(1 * 1000.toLong()) // 1 second, in milliseconds

        val mReceiver = MyBroadCastReciever()
        val screenStateFilter = IntentFilter()
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON)
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF)
        mainActivity!!.registerReceiver(mReceiver, screenStateFilter)

        preferenceManager = SharedPreferenceManager(mainActivity)
        userid = preferenceManager?.getUser()?.userID

        var id = preferenceManager?.getUser()?.userID!!.toDouble()
        userId = id!!.toInt()


        histroy()

        if (preferenceManager!!.gettype() != "parent") {
            references()
            binding?.one?.visibility = View.GONE
            binding?.linearLayoutCompat2?.visibility = View.GONE








            if (!BackgroundManager.getInstance().init(mainActivity)
                    .isServiceRunning(LockService::class.java)
            ) {
                BackgroundManager.getInstance().init(mainActivity)
                    .startService(LockService::class.java)
            }
            BackgroundManager.getInstance().init(mainActivity).startAlarmManager()
///////////////////////////////////
            if (DataManager.getInstance().hasPermission(getApplicationContext())) {
                MyAsyncTask(mainActivity!!).execute()
            }

            val mDPM = mainActivity!!.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            val mDeviceAdminRcvr = ComponentName(mainActivity!!, DevAdminReceiver::class.java)
            val active = mDPM.isAdminActive(mDeviceAdminRcvr)
            if (active) {

             }else{
                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                intent.putExtra(
                        DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                        mDeviceAdminRcvr
                )
                startActivityForResult(intent, 122)
            }

              checktime()

        } else {
            binding?.one?.visibility = View.VISIBLE
            binding?.childtime?.visibility = View.GONE

            BackgroundManager.getInstance().init(mainActivity).stopService(LockService::class.java)
            BackgroundManager.getInstance().init(mainActivity).stopAlarmManager()


        }
//        binding!!.start.setOnClickListener {
//             gettime()
//        }
        mLockInfoManager = CommLockInfoManager(mainActivity)
        mLockMainPresenter = LockMainPresenter(this, mainActivity)
        //  checkvale()
        setData()
        calldatalist()
        not_det = this
        return binding!!.root
    }

    private fun histroy() {

    }

    private fun references() {
        val tvPN = ContactsContract.Directory.PACKAGE_NAME
        var usern = mainActivity?.preferenceManager?.getUser()?.userEmail
        val separated: List<String> = usern!!.split("@")
        var username = separated[0]
        separated[1]
        var deviceTitle= mainActivity?.preferenceManager?.getdevicestitle()
        var devicesid =  mainActivity?.preferenceManager?.getdevicesid()
        val database = FirebaseDatabase.getInstance()
        val ui: Double = mainActivity?.preferenceManager!!.getUser()!!.userID!!.toDouble()
        val i = ui!!.toInt()
        val vi: Double = mainActivity?.preferenceManager!!.getdevicesid()!!.toDouble()
        val v = vi!!.toInt()
        val myRef1 = database.getReference("Devicesdata")
        val myRefoneparent = myRef1.child("Parent_" + username + "_" + i.toString())
        val Childdevices = myRefoneparent.child("Childdevices")
        val myRefone = Childdevices.child("Child_" + deviceTitle + "_" + v.toString())
         childclaim = myRefone.child("claimpoint");
         childclaimdata = myRefone.child("claimpoint");
         Allappsref = myRefone.child("Appsdata");

           if(mainActivity!!.preferenceManager!!.getfirsttimeservices()!!){
               firebaseappstatus  = true
               FirebaseData()
            }
//        val myRefoneapp = myRefone.child("Appsdata")
//        applistner(myRefoneapp)

        val fierbasetoken = myRefone.child("token");
        if(tokenfirebasedata.equals("")){
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(mainActivity!!) { instanceIdResult ->
                tokenfirebasedata = instanceIdResult.getToken()
                fierbasetoken.setValue(tokenfirebasedata)
            }
        }else{
            fierbasetoken.setValue(tokenfirebasedata)
        }






    }

    private fun checktime() {


        binding!!.timeswitch.setOnClickListener {
            if(timeswitch.isChecked){
                if( countDownTimer != null) {
                    countDownTimer!!.cancel()
                    countDownTimer!!.onFinish()
                }
                    gettime()
            }else{
                 mainActivity!!.preferenceManager!!.setstarttime(false)
               // // if(listener != null) {
                    if (countDownTimer != null) {
                        countDownTimer!!.cancel()
                        countDownTimer!!.onFinish()
                // // //  }
                }
                    stoptime(minutes_firebase)
                    //binding!!. binding!!.timer
            }

        }

        if(!mainActivity!!.preferenceManager!!.getsmarkschlock()!!) {
            if (mainActivity!!.preferenceManager!!.getstarttime()!!) {
                try {
                    var currenttime = System.currentTimeMillis()
                       var totaltime = mainActivity!!.preferenceManager!!.gettimecount()
                       if(totaltime == "")
                           totaltime = "0"
                       var totaltimeview = totaltime?.toLong()!!
                       if (totaltimeview!! > currenttime) {
                       binding!!.childtime.visibility = View.VISIBLE
                       binding!!.timeswitch.isChecked = true
                     var time = totaltime!!.toLong() - currenttime
                     startcounter(time)
                } else {
                    binding!!.childtime.visibility = View.GONE
                    mainActivity!!.preferenceManager!!.setstarttime(false)
                   //  stoptime(0)
                }
                }catch (e :Exception){
                    e.stackTrace
                }

              }else{
               // if(mainActivity!!.preferenceManager!!.getsmarkschlock()!!) {
                    gettimevissible()
                //}

            }
        }else{
            if(mainActivity!!.preferenceManager!!.getstarttime()!!){
                stoptime(0)
                }
                mainActivity!!.preferenceManager!!.setstarttime(false)
                binding!!.childtime.visibility = View.GONE

        }

    }

    private fun gettimevissible() {
        listenerdata = childclaimdata!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check for null
                if (dataSnapshot.value == null) {
                    binding!!.childtime.visibility = View.GONE
                    mainActivity!!.preferenceManager!!.setstarttime(false)
                    Log.e(AppBlockerFragment.TAG, "User data is null!")
                    return
                }
                var time = dataSnapshot.value.toString()
                if (time != "0") {
                    binding!!.timeseter.text = time
                    binding!!.childtime.visibility = View.VISIBLE
                    childclaimdata?.removeEventListener(listenerdata!!)

                } else {
                    mainActivity!!.preferenceManager!!.setstarttime(false)
                }


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e(AppBlockerFragment.TAG, "Failed to read user", error.toException())
            }
        })
    }

    private fun stoptime(minutes: Long?) {
        if(listener  == null){
            mainActivity!!.preferenceManager!!.setstarttime(false)
            childclaim!!.setValue(minutes)
        }else{
            childclaim?.removeEventListener(listener!!)
            mainActivity!!.preferenceManager!!.setstarttime(false)
            childclaim!!.setValue(minutes)


        }

     }

    private fun gettime() {


        listener = childclaim!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check for null
                if (dataSnapshot.value == null) {
                    binding!!.timeswitch.isChecked = false
                    Log.e(AppBlockerFragment.TAG, "User data is null!")

                    return
                }
                var time = dataSnapshot.value.toString()
                binding!!.timeseter.text = time
                if (time != "0") {
                    mainActivity!!.preferenceManager!!.setstarttime(true)
                    if (countDownTimer != null) {
                        countDownTimer!!.onFinish()
                    }
                    mainActivity!!.preferenceManager!!.settimeuse(time)
                    startcounter(time.toLong() * 60000)
                    var addtime = (time.toLong() * 60000)
                    mainActivity!!.preferenceManager!!.setnotification("0")
                    var currenttime = System.currentTimeMillis()
                    var totaltime = currenttime + addtime
                    mainActivity!!.preferenceManager!!.settimecount(totaltime.toString())
                } else {
                    binding!!.timeswitch.isChecked = false
                    mainActivity!!.preferenceManager!!.setstarttime(false)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(AppBlockerFragment.TAG, "Failed to read user", error.toException())
            }
        })
    }
    private fun startcounter(count: Long) {


        if (count.toInt() == 0) {
            binding!!.timer.setText("No Activity")

        } else {




            countDownTimer  = object : CountDownTimer(count, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    val seconds = (millisUntilFinished / 1000) % 60
                    val minutes = (millisUntilFinished / (1000 * 60) % 60)
                    val hours =   (millisUntilFinished / (1000 * 60 * 60) % 24)

                    val hh = if (hours < 10) "0$hours" else hours.toString() + ""
                    val mm = if (minutes < 10) "0$minutes" else minutes.toString() + ""
                    val ss = if (seconds < 10) "0$seconds" else seconds.toString() + ""



                    binding!!.timer.setText("$hh:$mm:$ss")
                    val m: Long = millisUntilFinished / 1000 / 60
                    minutes_firebase =  m

                    binding!!.timeseter.text = m.toString()


                    if(minutes < 15){
                        if(mainActivity!!.preferenceManager!!.getnotification().equals("0")){
                            mainActivity!!.preferenceManager!!.setnotification("1")
                            notification()
                        }

                    }


                }

                override fun onFinish() {
                    binding!!.timer.setText("done!")
                }
            }
            countDownTimer!!.onFinish()
            countDownTimer!!.start()



        }
    }

    private fun notification() {
        var token = ""
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(mainActivity!!) { instanceIdResult ->
           token = instanceIdResult.getToken()
        }
        var texts = "Time"
        val notification = JSONObject()
        val dataBody = JSONObject()
        val notificationBody = JSONObject()
        try{

            if(token != "") {
                dataBody.put("updateapp","updateapp")
                dataBody.put("ischeck","true")
                dataBody.put("bodymsg",texts)
                notificationBody.put("title", "Haipp")
                notificationBody.put("body", texts)
                notificationBody.put("click_action", "Haipp")
                notificationBody.put("notification_type", "Haipp")
                notification.put("to",  token)
                notification.put("priority", "high")
                notification.put("data", dataBody)
                notification.put("notification", notificationBody)
                mainActivity?.sendNotification(notification)
                navController.popBackStack()
            }else{
                Toast.makeText(mainActivity,"Please again select devices",Toast.LENGTH_LONG).show()
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun getDataList(tag: String?): List<SmartschedulModel?>? {
        var datalist: List<SmartschedulModel> = java.util.ArrayList<SmartschedulModel>()
        val gson = Gson()
        datalist = gson.fromJson<List<SmartschedulModel>>(
                tag,
                object : TypeToken<List<SmartschedulModel?>?>() {}.type
        )
        return datalist
    }

    private fun checkvale() {


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()

    }

    private fun setupSpinner() {
        binding!!.selctvalue.setOnClickListener(this)
        binding!!.select.onItemSelectedListener = this

    }


    private fun startTopToBottomAnimation() {
        val animation = AnimationUtils.loadAnimation(mainActivity, R.anim.slide_in_from_top)
        binding?.linearLayoutCompat2!!.startAnimation(animation)

    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)
    }

    private fun dataparse(C_date: String): Long {
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
        return 0

    }

    fun setData() {
        try {

            mainActivity!!.preferenceManager!!.setlockscreen(false)
            homeviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
            homeviewModel!!.userLiveData!!.observe(mainActivity!!, ReObserver())
            homeviewModel!!.loadDataNetwork("")
            cal_services = true



            if(preferenceManager!!.gettasksheetnav()!!) {
                preferenceManager!!.settasksheernav(false)!!
                preferenceManager!!.setworksheernav(false)!!
                if (mainActivity?.preferenceManager!!.gettype() == "parent"){
                    navController!!.navigate(R.id.taskFragment)
                 }else{
                    navController!!.navigate(R.id.taskChildFragment)
                }

            }else if(preferenceManager!!.getworksheetnav()!!){
                preferenceManager!!.setworksheernav(false)!!
                preferenceManager!!.settasksheernav(false)!!
                navController!!.navigate(R.id.worksheetFragment)

            }


        } catch (e: Exception) {
            mainActivity!!.toast("Login again", 0)
            e.stackTrace
        }
        //newsItems  = reponces
    }


    private fun FirebaseData() {
//        applistenerdata = Allappsref!!.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (firebaseappstatus) {
//                    UIHelper.showprogress(mainActivity)
//                    firebaseappstatus = false
//                    // Check for null
//                    if (dataSnapshot.value == null) {
//                        Log.e(AppBlockerFragment.TAG, "User data is null!")
//                        UIHelper.hidedailog()
//                        return
//                    }
//                    order = dataSnapshot.value as Map<String, ArrayList<Model>>
//                    selectedItems = ArrayList(order.get("Apps"))
//                    Log.i(AppBlockerFragment.TAG, "onDataChange: $selectedItems")
//                    finalAppdata = selectedItems
//
//                    for (i in 0 until selectedItems!!.size) {
//                        val item: HashMap<Any, Any> = selectedItems!!.get(i)
//                        for (info in commLockInfoslist) {
//                            if (info.packageName.equals(item.get("pacakagename").toString())) {
//                                val lockInfo: CommLockInfo = info
//                                changeItemLockStatus(item.get("isLocked").toString(), item.get("pacakagename").toString())
//                                break
//                            }
//                        }
//                        UIHelper.hidedailog()
//                    }
//                    Allappsref!!.removeEventListener(applistenerdata!!)
//                    //  usage_app(sortdatausage!!, i, v, username, deviceTitle, selectedItems!!,mainActivity)
//                    UIHelper.hidedailog()
//                }
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Failed to read value
//                UIHelper.hidedailog()
//                Log.e(AppBlockerFragment.TAG, "Failed to read user", error.toException())
//            }
//        })

        ////


        listdata = getsavearrray("app")
        applistenerdata = Allappsref!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check for null
                if (dataSnapshot.value == null) {
                    Log.e("TAG", "User data is null!")
                    return
                }
                order = dataSnapshot.value as Map<String, ArrayList<Model>>
                selectedItems = ArrayList(order.get("Apps"))
                Log.i("TAG", "onDataChange: $selectedItems")
                var dataSnapshot1: DataSnapshot
                dataSnapshot.value
                preferenceManager!!.setfirsttimservices(false)
                for (i in selectedItems!!.indices) {
                    val item: HashMap<Any, Any> = selectedItems!!.get(i)
                    for (j in listdata.indices) {
                        if (item["pacakagename"].toString() == listdata.get(j).packageName) {
                            changeItemLockStatus(item.get("isLocked").toString(), item.get("pacakagename").toString())
                            break
                        }
                    }
                }
                Allappsref!!.removeEventListener(applistenerdata!!)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "Failed to read user", error.toException())
            }
        })


    }

    private fun uploaddata() {

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
                Bitmap.createBitmap(
                        1,
                        1,
                        Bitmap.Config.ARGB_8888
                ) // Single color bitmap will be created of 1x1 pixel
            } else {
                Bitmap.createBitmap(
                        drawable.intrinsicWidth,
                        drawable.intrinsicHeight,
                        Bitmap.Config.ARGB_8888
                )
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun updateAppsList(usageStatsList: List<UsageStats?>, mainActivity: MainActivity) {
        val customUsageStatsList: MutableList<CustomUsageStats> =
            java.util.ArrayList<CustomUsageStats>()
        for (i in usageStatsList.indices) {
            val customUsageStats = CustomUsageStats()
            customUsageStats.usageStats = usageStatsList[i]
            try {
                val appIcon: Drawable = mainActivity.getPackageManager()
                    .getApplicationIcon(customUsageStats.usageStats.getPackageName())
                customUsageStats.appIcon = appIcon
            } catch (e: PackageManager.NameNotFoundException) {
                Log.w(
                        "TAG", java.lang.String.format(
                        "App Icon is not found for %s",
                        customUsageStats.usageStats.getPackageName()
                )
                )
                customUsageStats.appIcon = mainActivity.getDrawable(R.drawable.ic_launcher)
            }
            customUsageStatsList.add(customUsageStats)
        }
//        mUsageListAdapter.setCustomUsageStatsList(customUsageStatsList);
//        mUsageListAdapter.notifyDataSetChanged();
//        mRecyclerView.scrollToPosition(0);
    }

    fun changeItemLockStatus(
            @NonNull checkBox: String,
            @NonNull pacakge: String,
            ) {
        if (checkBox == "true") {
            mLockInfoManager?.lockCommApplication(pacakge)
        } else {
            mLockInfoManager?.unlockCommApplication(pacakge)
        }

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
            .queryUsageStats(
                    intervalType, cal.timeInMillis,
                    System.currentTimeMillis()
            )
        if (queryUsageStats.size == 0) {
            Log.i("TAG", "The user may not allow the access to apps usage. ")
//            mOpenUsageSettingButton.setVisibility(View.VISIBLE)
//            mOpenUsageSettingButton.setOnClickListener(View.OnClickListener {
//                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
//            })
        }
        return queryUsageStats
    }

    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()
        }
    }


    private inner class ReObserver : Observer<ArrayList<Adddevicesmodel>> {
        override fun onChanged(reponces: java.util.ArrayList<Adddevicesmodel>?) {
            if (!cal_services) return
            if (reponces == null) return
            cal_services = false


            devices.clear()
            newsItems = reponces
            for (item in reponces) {
                devices.add(item.device_title.toString())
            }

            selectAdapter =
                ArrayAdapter<String?>(mainActivity!!, android.R.layout.simple_spinner_item, devices)
            selectAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding!!.select.adapter = selectAdapter

        }
    }

    private inner class LocationObserver : Observer<LastModel> {
        override fun onChanged(reponces: LastModel?) {
            if (!location_services) return
            if (reponces == null) return
            location_services = false

            loactionsetmap(reponces.cc_latitude, reponces.cc_longitude)


        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mGoogleApiClient = GoogleApiClient.Builder(mainActivity!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onMapReady(p0: GoogleMap?) {
        map = p0;
        map!!.getUiSettings().setMyLocationButtonEnabled(false);
        if (ActivityCompat.checkSelfPermission(
                        activity!!,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        activity!!,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }


        map!!.setMyLocationEnabled(true);
        map!!.setOnCameraChangeListener(this)

    }

    override fun onResume() {
        super.onResume()
        setUpMapIfNeeded();
        mGoogleApiClient?.connect();
        binding?.mapView?.onResume();
    }

    private fun setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.

            if (map != null) {
                setUpMap()
            }
        }
    }


    private fun setUpMap() {
        map?.addMarker(MarkerOptions().position(LatLng(0.0, 0.0)).title("Location"))
    }

    override fun onStart() {
        super.onStart()
        binding?.mapView?.onStart();
    }

    override fun onPause() {
        super.onPause()
        binding?.mapView?.onPause();
    }

    override fun onStop() {
        super.onStop()
        binding?.mapView?.onStop();
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.mapView?.onLowMemory();
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.mapView?.onDestroy();

        if (listener != null) {
            myRefoneapp?.removeEventListener(listener!!)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding?.mapView?.onSaveInstanceState(outState);
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.txtTitleName?.text = "Home"
        titleBar?.btnLeft?.visibility = View.GONE
        titleBar?.setBtnRight(R.drawable.notification, View.OnClickListener {
            navController!!.navigate(R.id.notificationFragment)
        });

        titleBar?.setBtnLeft(R.drawable.website, View.OnClickListener {

//            val intent2 = Intent(mainActivity, MainActivity2::class.java)
//            startActivity(intent2)




        });

    }

    override fun onConnected(p0: Bundle?) {
        if (mGoogleApiClient != null) {
            try {


                val location: Location =
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
                if (ActivityCompat.checkSelfPermission(
                                mainActivity!!,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                mainActivity!!,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }

                location.let { handleNewLocation(it) }
                    ?: LocationServices.FusedLocationApi.requestLocationUpdates(
                            mGoogleApiClient,
                            mLocationRequest,
                            this
                    )
            } catch (e: java.lang.Exception) {
                e.stackTrace
            }
        }
    }

    private fun handleNewLocation(location: Location) {

        Log.d("TAG", location.toString())
        val currentLatitude = location.latitude
        val currentLongitude = location.longitude
        val latLng = LatLng(currentLatitude, currentLongitude)

        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        val options = MarkerOptions()
            .position(latLng)
            .title("I am here!")

        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        map?.animateCamera(CameraUpdateFactory.zoomTo(14.0f))


    }

    private fun loactionsetmap(ccLatitude: String?, ccLongitude: String?) {
        val lat: String = ccLatitude.toString()
        val lon: String = ccLongitude.toString()
        //  var latitude = lat.substring(0, lat.indexOf(".") + 6).toDouble()
        //  var longitude = lon.substring(0, lon.indexOf(".") + 6).toDouble()


        val latLng = LatLng(ccLatitude!!.toDouble(), ccLongitude!!.toDouble())

        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        val options = MarkerOptions()
            .position(latLng)
            .title("I am here!")

        map?.addMarker(options)
        // map?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        //map?.animateCamera(CameraUpdateFactory.zoomTo(14.0f))


        val location: CameraUpdate = CameraUpdateFactory.newLatLngZoom(
                latLng, 15f
        )
        map?.animateCamera(location)

    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onLocationChanged(p0: Location?) {
        handleNewLocation(p0!!);

    }

    override fun onCameraChange(p0: CameraPosition?) {
        // mCameraHandler?.removeCallbacks(mCameraMoveCallback)
        //  mCameraHandler?.postDelayed(mCameraMoveCallback, 400)
    }

    private val mCameraMoveCallback = Runnable {
        try {
            if (Geocoder.isPresent() && isNetworkConnected()) {
                val position: CameraPosition = map!!.getCameraPosition()
                GeocoderHelperClass(activity).setResultInterface(object :
                        GeocoderHelperClass.SetResult {
                    override fun onGetResult(list: List<Address?>) {
                        Log.d("TAG", "onGetResult: ")
                        val address: String = list.get(0)!!.getAddressLine(0)
                        if (address != null && !address.isEmpty()) {
//                            mCurrentLocation = AddressModel(list[0], AddressModel.TYPE_ADDRESS)
                            binding!!.txtCurrentLocation.text = address.toUpperCase(Locale.US)
                            return
                        }
                    }
                }).execute(position.target.latitude, position.target.longitude)
            } else {
                binding!!.txtCurrentLocation.text =
                    "Cannot find address. Please check internet connection."
                //mCurrentLocation = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            mainActivity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (spinnerCheck++ > 0) {
            binding!!.selctvalue.text = newsItems[position].device_title
            if (mainActivity?.preferenceManager!!.gettype() == "parent") {
                homeselectchild = true
                map?.clear()
                mainActivity?.preferenceManager!!.setdevicesid(newsItems[position].devicesid)
                mainActivity?.preferenceManager!!.setdevicetitle(newsItems[position].device_title)

                homeviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
                homeviewModel!!.lastlocation!!.observe(mainActivity!!, LocationObserver())
                homeviewModel!!.loadloacation(newsItems[position].devicesid!!)
                location_services = true
                var devicesid = newsItems[position].devicesid
                var devicetittle = newsItems[position].device_title
                firebase(devicesid,devicetittle)
            }
        }
    }
    private fun firebase(devicesid: String?, deviceTitle: String?) {
        UIHelper.showprogress(mainActivity)
        val tvPN = ContactsContract.Directory.PACKAGE_NAME
        var usern = mainActivity?.preferenceManager?.getUser()?.userEmail
        val separated: List<String> = usern!!.split("@")
        var username = separated[0]
        separated[1]
//        var deviceTitle= mainActivity?.preferenceManager?.getdevicestitle()
//        var devicesid =  mainActivity?.preferenceManager?.getdevicesid()
        val database = FirebaseDatabase.getInstance()
        val ui: Double = mainActivity!!.preferenceManager!!.getUser()!!.userID!!.toDouble()
        val i = ui!!.toInt()
        val vi: Double = devicesid!!.toDouble()
        val v = vi!!.toInt()
        val myRef1 = database.getReference("Devicesdata")
        val myRefoneparent = myRef1.child("Parent_" + username + "_" + i.toString())
        val Childdevices = myRefoneparent.child("Childdevices")
        val myRefone = Childdevices.child("Child_" + deviceTitle + "_" + v.toString())
        childtokenref = myRefone.child("token");
        tokenlistener = childtokenref!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check for null
                if (dataSnapshot.value == null) {
                    Log.e("TAG", "User data is null!")
                    DbContract.install = ""
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


    }





    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    fun getArrayList(key: String?): java.util.ArrayList<Model?>? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity)
        val gson = Gson()
        val json = prefs.getString(key, null)
        val type =
            object : TypeToken<java.util.ArrayList<CommLockInfo?>?>() {}.type
        return gson.fromJson<java.util.ArrayList<Model?>>(json, type)
    }
    fun getsavearrray(key: String?): ArrayList<CommLockInfo> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val json = prefs.getString(key, null)
        val type = object : TypeToken<java.util.ArrayList<CommLockInfo?>?>() {}.type
        return gson.fromJson(json, type)
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.gender -> binding!!.select.performClick()
            R.id.task -> {

                if(homeselectchild) {
                    if (mainActivity!!.preferenceManager!!.getdevicesid() != null) {
                        if (preferenceManager?.getdevicesid() != "")
                            navController!!.navigate(R.id.taskFragment)
                    }
                }else{
                    mainActivity!!.toast("Please select kid devices",0)
                }
            }
            R.id.worksheet -> {
                Log.i("TAG", "onClick: "+DbContract.childtoken)
                if(homeselectchild) {
                    if (mainActivity!!.preferenceManager!!.getdevicesid() != null) {
                        if (preferenceManager?.getdevicesid() != "")
                            navController!!.navigate(R.id.worksheetFragment)
                    }
                }else{
                    mainActivity!!.toast("Please select kid devices",0)
                }
            }
            R.id.smartsch -> {
                if(homeselectchild) {
                    if (mainActivity!!.preferenceManager!!.getdevicesid() != null) {
                        if (preferenceManager?.getdevicesid() != "")
                            navController!!.navigate(R.id.smartscheduleFragment2)
                    }
                }else{
                    mainActivity!!.toast("Please select kid devices",0)
                }
            }
            R.id.actreport -> {
                if(homeselectchild) {
                    if (mainActivity!!.preferenceManager!!.getdevicesid() != null) {
                        if (preferenceManager?.getdevicesid() != "")
                            navController!!.navigate(R.id.reportFragment)
                    }
                }else{
                    mainActivity!!.toast("Please select kid devices",0)
                }
            }

            R.id.selctvalue -> {
                binding?.select?.performClick()
            }


        }
    }

    override fun onMovieClicked(count: Int) {
        Toast.makeText(mainActivity, Int.toString(), Toast.LENGTH_SHORT).show();
    }

    override fun loadAppInfoSuccess(list: MutableList<CommLockInfo>?) {

    }


    private fun calldatalist() {

        if(mArrayList_count != null)
            mArrayList_count.clear()

        database = FirebaseFirestore.getInstance()

        val usersdata = database!!.collection("Threads")
        val query = usersdata.whereEqualTo("loginUserId", userId.toString())
        try {
            query.get().addOnSuccessListener { documentSnapshot ->

//            if (documentSnapshot != null) {
//                Log.d("FirestoreSourceSet", "Get document success")
//            } else {
//                Log.e("FirestoreSourceSet", "Get document error", documentSnapshot)
//            }

                if (documentSnapshot != null) {
                    mArrayList.clear()
                    mArrayList_unread.clear()
                    val types = documentSnapshot.toObjects(Messangermodel::class.java)
                    mArrayList.addAll(types)
                    flgcall = true
                    try {
                        if(mArrayList.size > 0)
                            mArrayList.sortWith(Timesortmessanger())

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    callotherdata()

//                        messangerAdapter = MessangerAdapter(mainActivity!!, mArrayList, userId)
//                        binding!!.recycler.adapter = messangerAdapter


                } else {


                }
            }.addOnFailureListener {
                Log.w("TAG", "Failed to register SMS listener.");

            }
        }catch (e :Exception){
            e.stackTrace
        }



    }
    private fun callotherdata() {

        database = FirebaseFirestore.getInstance()
        val usersdata = database!!.collection("Threads")
        val query = usersdata.whereEqualTo("otherUserId", userId.toString())
        query.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null) {
                val types = documentSnapshot.toObjects(Messangermodel::class.java)
                mArrayList.addAll(types)
                flgcall = false
                try {
                    if(mArrayList.size > 0)
                        mArrayList.sortWith(Timesortmessanger())
                    Collections.reverse(mArrayList)

                } catch (e: Exception) {
                    UIHelper.hidedailog()
                    e.printStackTrace()
                }

                //  callotherdata()
                for (i in mArrayList!!.indices) {
                    if(mArrayList.get(i).getOtherUserId() == userId.toInt().toString()){
                        var id1 = mArrayList.get(i).getOtherUserId()
                        var id2 = mArrayList.get(i).getLoginUserId()

                        var iimage1 = mArrayList.get(i).getOtherUserImage()
                        var iimage2 = mArrayList.get(i).getLoginUserImage()

                        var itype1 = mArrayList.get(i).getOtherUserType()
                        var itype2 = mArrayList.get(i).getLoginUserType()

                        var iname1 = mArrayList.get(i).getOtherUserName()
                        var iname2 = mArrayList.get(i).getLoginUserName()

                        mArrayList.get(i).setLoginUserId(id1)
                        mArrayList.get(i).setOtherUserId(id2)

                        mArrayList.get(i).setLoginUserImage(iimage1)
                        mArrayList.get(i).setOtherUserImage(iimage2)

                        mArrayList.get(i).setLoginUserType(itype1)
                        mArrayList.get(i).setOtherUserType(itype2)

                        mArrayList.get(i).setLoginUserName(iname1)
                        mArrayList.get(i).setOtherUserName(iname2)

                        calldataunmsg(id2!!)
                    }

                }

                //crash



            } else {


            }


        }.addOnFailureListener {

        }

    }
    private fun calldataunmsg(id: String) {
        var A :Int = id!!.toInt()
        var B :Int = userId!!.toInt()
        if(A < B){
            greaterid = B.toString()
            smallerid = A.toString()
            Log.d("TAG", "onCreateView: " + A)
        }else{
            greaterid = A.toString()
            smallerid = B.toString()
        }
        Gettingdataone()
    }
    private fun calldatareset() {
        var count = 1

        try{
            if(mArrayList_unread.size != 0){
                for (i in mArrayList!!.indices) {
                    for (k in mArrayList_unread!!.indices) {
                        if(mArrayList.get(i).getOtherUserId() == mArrayList_unread.get(k)!!.getSenderID()){
                            mArrayList.get(i).setisSeen(false)
                            Log.d(ContentValues.TAG, "onSuccess: $")
                            mArrayList.get(i).setcount(mArrayList_unread.count())
                        }

                    }

                }


            }

        }catch (e:Exception){
            // Toast.makeText(mainActivity,e.toString(),Toast.LENGTH_LONG).show()

        }
    }
    private fun Gettingdataone(): String? {
        var id: String? = null
        val citiesRef = database!!.collection("Chats")
        val query = citiesRef.whereArrayContainsAny("users", Arrays.asList(smallerid, greaterid))
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    id = document.id
                    //sendMessage(view,content,postimages)
                    calling_array(id!!)
                }

            }
        }
        Log.i(ContentValues.TAG, "Gettingdata: $query")
        return null
    }

    private fun calling_array(id: String) {
        ///this references call
        val contactListener = database!!.collection("Chats").document(id!!)
        contactListener.addSnapshotListener(EventListener { documentSnapshot, e ->
            if (e != null) {
                // Log.d("ERROR", e.message)
                return@EventListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                Log.i(ContentValues.TAG, "onEvent: " + documentSnapshot.data)
                documentsnap = documentSnapshot
                getdatafirestore(id)
            }
        })
    }
    private fun getdatafirestore(id: String?) {
        /// UYc0DfCuKXdpOa1WsZoC
        database2 = FirebaseFirestore.getInstance()
        database2!!.collection("Chats").document(id!!).collection("thread").get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    val types = documentSnapshot.toObjects(FirebasetimeClass::class.java)
                    mArrayList_count.clear()
                    // val types = documentSnapshot.toObjects(Messtest::class.java)
                    mArrayList_count.addAll(types)

                    if(mainActivity!!.preferenceManager?.gettype() != "parent"){
                        checkid = mainActivity!!.preferenceManager!!.getdevicesid()!!
                    }else{
                        checkid = mainActivity!!.preferenceManager!!.getUser()!!.userID!!
                    }
                    Log.i("TAG", "getdatafirestore: "+checkid)
                    Log.d(ContentValues.TAG, "onSuccess: $mArrayList_count")
                    var total : Int = 0
                    mArrayList_unread.clear()
                    for (i in mArrayList_count!!.indices) {
                        if (mArrayList_count[i]?.getisSeen() == false){
                            total = + 1

                            if(checkid != mArrayList_count[i]?.getSenderID()){
                                mArrayList_unread.add(mArrayList_count[i])
                                mainActivity!!.chatfirebase(mArrayList_unread.size)}
                            //  var t = documentSnapshot.documents[i].id

                        }
                    }

                } else {

                }
                calldatareset()

            }.addOnFailureListener {

            }

    }
    fun callworksheet() {
        navController!!.navigate(R.id.worksheet)
    }
    @SuppressLint("StaticFieldLeak")
    class MyAsyncTask(var mainActivity: MainActivity) : AsyncTask<Void, Void, List<AppItem>>() {
        var mymonitor: DatabaseReference? = null
        override fun doInBackground(vararg params: Void?): List<AppItem>? {
            return DataManager.getInstance().getApps(mainActivity, 2, 0)
        }

        override fun onPreExecute() {
            super.onPreExecute()
            UIHelper.showprogress(mainActivity)

            val tvPN = PACKAGE_NAME
            var usern = mainActivity?.preferenceManager?.getUser()?.userEmail


            val separated: List<String> = usern!!.split("@")
            var username = separated[0]
            separated[1]
            var deviceTitle = mainActivity?.preferenceManager?.getdevicestitle()
            var devicesid = mainActivity?.preferenceManager?.getdevicesid()
            val database = FirebaseDatabase.getInstance()
            val ui: Double = userid!!.toDouble()
            val i = ui!!.toInt()
            val vi: Double = devicesid!!.toDouble()
            val v = vi!!.toInt()
            val myRef1 = database.getReference("Devicesdata")
            val myRefoneparent = myRef1.child("Parent_" + username + "_" + i.toString())
            val Childdevices = myRefoneparent.child("Childdevices")
            val myRefone = Childdevices.child("Child_" + deviceTitle + "_" + v.toString())
            mymonitor = myRefone.child("AppsMonitor");


            // ...
        }

        override fun onPostExecute(appItems: List<AppItem>) {
            super.onPostExecute(appItems)
            UIHelper.hidedailog()
            mymonitor?.setValue(appItems)
            // ...
        }
    }

}


//internal class MyAsyncTask : AsyncTask<Int?, Void?, List<AppItem>>() {
//    override fun onPreExecute() {
//        super.onPreExecute()
//
//    }
//
//    protected override fun doInBackground(vararg integers: Int?): List<AppItem>? {
//        return DataManager.getInstance().getApps(getApplicationContext(), 2, 0)
//    }
//
//    override fun onPostExecute(appItems: List<AppItem>) {
//        mTotal = 0
//        for (item in appItems) {
//            if (item.mUsageTime <= 0) continue
//            mTotal += item.mUsageTime
//          //  item.mCanOpen = mPackageManager.getLaunchIntentForPackage(item.mPackageName) != null
//        }
//
//
//       ///mAdapter.updateData(appItems)
//    }
//}

internal enum class StatsUsageInt(private val mStringRepresentation: String, val mInterval: Int) {
    DAILY("Daily", UsageStatsManager.INTERVAL_DAILY), WEEKLY(
            "Weekly",
            UsageStatsManager.INTERVAL_WEEKLY
    ),
    MONTHLY("Monthly", UsageStatsManager.INTERVAL_MONTHLY), YEARLY(
            "Yearly",
            UsageStatsManager.INTERVAL_YEARLY
    );

    companion object {

        fun getValue(stringRepresentation: String): StatsUsageInt? {
            for (statsUsageInterval in StatsUsageInt.values()) {
                if (statsUsageInterval.mStringRepresentation == stringRepresentation) {
                    return statsUsageInterval
                }
            }
            return null
        }
    }

}
//
//@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//private fun usage_app(
//    sortdatausage: java.util.ArrayList<AppInfo>,
//    i: Int,
//    v: Int,
//    username: String,
//    deviceTitle: String?,
//    listdata: java.util.ArrayList<Model>?,
//    mainActivity: MainActivity?
//) {
//    var databasearray: java.util.ArrayList<Model>? = null
//
//    val database = FirebaseDatabase.getInstance()
//    val myRef1 = database.getReference("Devicesdata")
//    val myRefoneparent = myRef1.child("Parent_" + username + "_" + i.toString())
//    val Childdevices = myRefoneparent.child("Childdevices")
//    val myRefone = Childdevices.child("Child_" + deviceTitle + "_" + v.toString())
//    val map: HashMap<String, java.util.ArrayList<Model>> =
//        HashMap<String, java.util.ArrayList<Model>>()
//    databasearray = java.util.ArrayList<Model>()
//    UIHelper.showprogress(mainActivity)
//    for (i in listdata!!.indices) {
//          val item: HashMap<Any,Any> = listdata!!.get(i)
//        for (j in 0 until sortdatausage?.size!!) {
//
//            if (item.get("pacakagename").toString() == sortdatausage?.get(j)?.pacakagename) {
//                val apps = Model()
//                apps.set("totaltime", sortdatausage!!.get(j).toltaltime)
//                apps.set("pacakagename", item.get("pacakagename").toString())
//                 apps.set("icon", item.get("icon").toString())
//                 apps.set("appname",item.get("appname").toString())
//                 apps.set("parent_id", i)
//                 apps.set("Child_id", v)
//                 databasearray!!.add(apps)
//
//            }
//
//        }
//    }
////            map["usage_app"] == databasearray
//       map.put("usage_app", databasearray!!)
//    Log.i(ContentValues.TAG, "usage_app: $databasearray")
//    val myRefoneapp = myRefone.child("Apps_usage");
//    myRefoneapp.setValue(map)
//    UIHelper.hidedailog()
//
//}


private class LastTimeLaunchedComparatorDesc(date: Long) : Comparator<UsageStats?> {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun compare(o1: UsageStats?, o2: UsageStats?): Int {
        return java.lang.Long.compare(o2!!.lastTimeUsed, o2.lastTimeUsed)
    }
}


////
//                    if(selectedItems!!.size == commLockInfoslist.size){
//                        appsinstallercheck = true
//                    }

//                    if(appsinstallercheck!!){
//                        appsinstallercheck = true
//                        val pm: PackageManager = mainActivity!!.getPackageManager()
//                        myAppsToUpdate = java.util.ArrayList<AppInfo>()
//                        for (i in 0 until commLockInfoslist.size) {
//                            for (j in 0 until selectedItems!!.size) {
//
//                                if (!selectedItems!!.get(j).packageName!!.equals(commLockInfoslist.get(i).packageName)) {
//
//                                        val appInfo = AppInfo()
//                                        appInfo.setAppname(commLockInfoslist.get(i).appName)
//                                        appInfo.setPacakagename(commLockInfoslist.get(i).packageName)
//                                        appInfo.setHide("0")
//                                            try {
//                                                var appin: PackageInfo? = null
//                                                appin = pm.getPackageInfo(commLockInfoslist.get(i).packageName!!, 0)
//                                                appInfo.icon = appin?.applicationInfo?.loadIcon(pm)
//                                                val drawable = appin?.applicationInfo?.loadIcon(pm)
//                                                binding?.img?.setImageDrawable(appin?.applicationInfo?.loadIcon(pm))
//                                                if(drawable != null)
//                                                    bitmaps = drawableToBitmap(drawable!!)
//                                                     myAppsToUpdate?.add(appInfo)
//
//                                            } catch (e: PackageManager.NameNotFoundException) {
//                                                e.printStackTrace()
//                                            }
//
//                                    try {
//                                        val storage = FirebaseStorage.getInstance()
//                                        val baos = ByteArrayOutputStream()
//                                        bitmaps!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//                                        val data = baos.toByteArray()
//                                        val storageRef = storage.getReferenceFromUrl("gs://haipp-6a7d3.appspot.com")
//                                        val imagesRef = storageRef.child("images/" + commLockInfoslist.get(i).toString().toString() + ".jpg")
//                                        val uploadTask = imagesRef.putBytes(data)
//                                        val finalMyAppsToUpdate: java.util.ArrayList<AppInfo> = myAppsToUpdate!!
//                                        uploadTask.addOnSuccessListener { taskSnapshot ->
//                                            val downloadUrlss = taskSnapshot.storage.downloadUrl
//                                            val uri = taskSnapshot.storage.downloadUrl
//                                            while (!uri.isComplete);
//                                            val url = uri.result
//                                            Log.i(ContentValues.TAG, "onSuccess: $downloadUrlss")
//                                            Log.i(ContentValues.TAG, "onSuccess: $url")
//                                            Log.d("downloadUrl-->", "" + downloadUrlss)
//                                            count++
//
//                                            val applist = Model()
//                                            applist.set("pacakagename", myAppsToUpdate!!.get(count-1).pacakagename)
//                                            applist.set("hide", "0")
//                                            applist.set("isLocked", false)
//                                            applist.set("appname", myAppsToUpdate!!.get(count-1).appname)
//                                            applist.set("unistall", "0")
//                                            applist.set("icon", url.toString())
//                                            finalAppdata?.add(applist)
//                                            if (count == finalMyAppsToUpdate.size) {
//
//
//                                            }
//                                        }
//                                                .addOnFailureListener {
//
//                                                }
//                                                .addOnProgressListener { taskSnapshot ->
//
//                                                }
//
//
//                                    } catch (e: Exception) {
//                                        e.stackTrace
//                                    }
//
//                                } else {
//                                    //do something for equals
//                                }
//
//                            }
//                        }
//
//                    }else{


///
//


//if (preferenceManager!!.gettype() != "parent") {
//
//
//    val tvPN = PACKAGE_NAME
//    var usern = mainActivity?.preferenceManager?.getUser()?.userEmail
//
//
//    val separated: List<String> = usern!!.split("@")
//    var username = separated[0]
//    separated[1]
//    var deviceTitle = mainActivity?.preferenceManager?.getdevicestitle()
//    var devicesid = mainActivity?.preferenceManager?.getdevicesid()
//    val database = FirebaseDatabase.getInstance()
//    val ui: Double = userid!!.toDouble()
//    val i = ui!!.toInt()
//    val vi: Double = devicesid!!.toDouble()
//    val v = vi!!.toInt()
//    val myRef1 = database.getReference("Devicesdata")
//    val myRefoneparent = myRef1.child("Parent_" + username + "_" + i.toString())
//    val Childdevices = myRefoneparent.child("Childdevices")
//    val myRefone = Childdevices.child("Child_" + deviceTitle + "_" + v.toString())
//    myRefoneapp = myRefone.child("Appsdata");
//    mainActivity!!.preferenceManager?.setchildref(myRefoneapp.toString())
//    var currentTime = System.currentTimeMillis()
//    var C_date = convertLongToTime(currentTime)
//    var date = dataparse(C_date)
//    Log.i("TAG", "setData: " + date)
//    mDPM = mainActivity?.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
//    mDeviceAdminRcvr = ComponentName(mainActivity!!, DevAdminReceiver::class.java)
//    FirebaseData(sortdatausage!!, i, v, username, deviceTitle)
//} else {
//
//}