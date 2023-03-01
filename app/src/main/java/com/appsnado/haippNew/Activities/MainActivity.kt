package com.appsnado.haippNew.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager

import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.appsnado.haippNew.AppBlockerActivity.AppBlockerFragment
import com.appsnado.haippNew.Applocakpacakges.base.AppConstants
import com.appsnado.haippNew.Applocakpacakges.db.CommLockInfoManager
import com.appsnado.haippNew.Applocakpacakges.model.CommLockInfo
import com.appsnado.haippNew.Applocakpacakges.mvp.contract.LockMainContract
import com.appsnado.haippNew.Applocakpacakges.mvp.p.LockMainPresenter
import com.appsnado.haippNew.Applocakpacakges.services.BackgroundManager
import com.appsnado.haippNew.Applocakpacakges.services.LockService
import com.appsnado.haippNew.Applocakpacakges.services.LockService.settinglockpermission
import com.appsnado.haippNew.Applocakpacakges.widget.DialogPermission
import com.appsnado.haippNew.Applocakpacakges.widget.DialogSearch
import com.appsnado.haippNew.Chat.ChatFragmentDirections
import com.appsnado.haippNew.Chat.FirebasetimeClass
import com.appsnado.haippNew.Chat.Messangermodel
import com.appsnado.haippNew.Comman_Pacakges.data.DbContract
import com.appsnado.haippNew.Firebase.Model
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModel
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.Setting.SettingFragmentDirections
import com.appsnado.haippNew.baseactivity.BaseActivity
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.data.SharedPreferenceManager
import com.appsnado.haippNew.databinding.MainBinding
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.retro.WebServiceConstants
import com.appsnado.haippNew.retro.WebServiceConstants.devices_id
import com.appsnado.haippNew.taskChild.TaskChildFragmentDirections
import com.appsnado.haippNew.taskParent.TaskDetailFragment
import com.facebook.FacebookSdk
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.internal.ConnectionCallbacks
import com.google.android.gms.common.api.internal.OnConnectionFailedListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.theartofdev.edmodo.cropper.CropImage
import com.thekhaeng.pushdownanim.PushDownAnim
import com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.*
import kotlin.Throws
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import com.android.volley.AuthFailureError

import com.android.volley.VolleyError
import com.appsnado.haippNew.HomeFragmentDirections
import com.appsnado.haippNew.MySingleton
import com.appsnado.haippNew.R


class MainActivity : BaseActivity(), ConnectionCallbacks, OnConnectionFailedListener,
    GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
    LockMainContract.View, View.OnClickListener {
    var Radio_1: RadioButton? = null
    var Radio_2: RadioButton? = null
    var Radio_3: RadioButton? = null
    var Radio_4: RadioButton? = null
    var cttext: CircularProgressBar? = null
    var cTimer: CountDownTimer? = null
    var binding: MainBinding? = null
    var cal_logout: Boolean = false

    private val REQUEST_READ_PHONE_STATE = 1

    //val map: Map<String, Model> = HashMap()
    var appdata: ArrayList<Model>? = null
    var map: HashMap<String, ArrayList<Model>> = HashMap<String, ArrayList<Model>>()
    var googleApiClient: GoogleApiClient? = null

    ///
    var greaterid: String? = null
    var smallerid: String? = null

    private val RESULT_ACTION_IGNORE_BATTERY_OPTIMIZATION = 351
    private val TAG = "MainActivity"
    private val mTopLayout: RelativeLayout? = null
    private val mBtnSetting: ImageView? = null
    private val mEditSearch: TextView? = null
    private val mTabLayout: TabLayout? = null
    private val mViewPager: ViewPager? = null
    private var mLockMainPresenter: LockMainPresenter? = null
    private var mDialogSearch: DialogSearch? = null
    private var mLockInfoManager: CommLockInfoManager? = null

    private val titles: List<String>? = null
    private val fragmentList: List<Fragment>? = null
    private val RESULT_ACTION_USAGE_ACCESS_SETTINGS = 1


    private val mArrayList_count =
        java.util.ArrayList<FirebasetimeClass?>()
    private val mArrayList_unread =
        java.util.ArrayList<FirebasetimeClass?>()
    //
    private var database2: FirebaseFirestore? = null
    private var database: FirebaseFirestore? = null
    var documentsnap : DocumentSnapshot? = null

    val mArrayList = java.util.ArrayList<Messangermodel>()

    private var flgcall : Boolean? = false

    @JvmField
    @BindView(R.id.header_main)
    var titleBar: TitleBar? = null

    val mDeviceAdminRcvr: DevicePolicyManager? = null
    var id :Double = 0.0
    var userId :Int = 0

        @JvmField
    var preferenceManager: SharedPreferenceManager? = null
    private var mMainViewModel: MainViewModel? = null
    var timer = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        preferenceManager = SharedPreferenceManager(this)
        ButterKnife.bind(this)
        FirebaseApp.initializeApp(this);
        FacebookSdk.sdkInitialize(this);
        permissions();
        setupGoogleSignIn()
        calltoken()
        getDeviceToken()
        if (settinglockpermission) {
            val dialog = DialogPermission(this!!)
            dialog.show()
            dialog.setOnClickListener {
                settinglockpermission = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    var intent: Intent? = null
                    intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                    startActivityForResult(
                        intent,
                        RESULT_ACTION_USAGE_ACCESS_SETTINGS
                    )
                }
            }
        }

//        if (DataManager.getInstance().hasPermission(this)) {
//            Log.i(TAG, "onCreate: "+"")
//            ///HomeFragment.MyAsyncTask(mainActivity!!).execute()
//        }

        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, this) }
        mMainViewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        binding?.navigationViewTop?.visibility = View.GONE
        val host = NavHostFragment.create(R.navigation.nav_graph)

        mLockInfoManager = CommLockInfoManager(this)
        mLockMainPresenter = LockMainPresenter(this, this)
        mLockMainPresenter?.loadAppInfo(this)



        binding!!.basketCount.visibility = View.GONE

        PushDownAnim.setPushDownAnimTo(home).setScale(MODE_SCALE, 1.20f).setDurationPush(100)
            .setDurationRelease(150).setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
            .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR).setOnClickListener {
//                    navController.popBackStack(R.id.settingFragment, true);
//                    navController.popBackStack(R.id.featureFragment, true);
//                    navController.popBackStack(R.id.chatFragment2, true);

                navController!!.navigate(R.id.homeFragment)
                redview = false

                //supportFragmentManager.beginTransaction().replace(R.id.homeFragment,host).setPrimaryNavigationFragment(host!!).commit()

            }
        PushDownAnim.setPushDownAnimTo(feature).setScale(MODE_SCALE, 1.20f).setDurationPush(100)
            .setDurationRelease(150).setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
            .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR).setOnClickListener {
                redview = false
//                    navController.popBackStack(R.id.settingFragment, true);
//                    navController.popBackStack(R.id.homeFragment, true);
//                    navController.popBackStack(R.id.chatFragment2, true);
                navController!!.navigate(R.id.featureFragment)

                // applauncher("1")
                //  supportFragmentManager.beginTransaction().replace(R.id.featureFragment,host).setPrimaryNavigationFragment(host!!).commit()
            }
        PushDownAnim.setPushDownAnimTo(chat).setScale(MODE_SCALE, 1.20f).setDurationPush(100)
            .setDurationRelease(150).setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
            .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR).setOnClickListener {
//                    navController.popBackStack(R.id.settingFragment, true);
//                    navController.popBackStack(R.id.homeFragment, true);
//                    navController.popBackStack(R.id.featureFragment, true);
               // if (spinnerCheck > 0) {

                redview = true
                if (preferenceManager!!.gettype() == "parent") {
                    if (!preferenceManager!!.getdevicesid().isNullOrEmpty()) {
                        navController!!.navigate(R.id.chatFragment2)
                        binding!!.basketCount.visibility = View.GONE

                    }else{
                        toast("Select Device",0)

                    }

                }else{
                    binding!!.basketCount.visibility = View.GONE

                    navController!!.navigate(R.id.chatFragment2)
                }

            }
        PushDownAnim.setPushDownAnimTo(setting).setScale(MODE_SCALE, 1.20f).setDurationPush(100)
            .setDurationRelease(150).setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
            .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR).setOnClickListener {
//                    navController.popBackStack(R.id.homeFragment, true);
//                    navController.popBackStack(R.id.featureFragment, true);
//                    navController.popBackStack(R.id.chatFragment2, true);
                redview = false
                   navController!!.navigate(R.id.settingFragment)
                // supportFragmentManager.beginTransaction().replace(R.id.settingFragment,host).setPrimaryNavigationFragment(host!!).commit()
            }

        PushDownAnim.setPushDownAnimTo(task).setScale(MODE_SCALE, 1.20f).setDurationPush(100)
            .setDurationRelease(150).setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
            .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR).setOnClickListener {

                ///  navController.popBackStack(R.id.homeFragment, true);
                //navController.popBackStack(R.id.chatFragment2, true);

                redview = false
                navController!!.navigate(R.id.taskChildFragment)
                // supportFragmentManager.beginTransaction().replace(R.id.task,host).setPrimaryNavigationFragment(host!!).commit()
            }

        PushDownAnim.setPushDownAnimTo(logout).setScale(MODE_SCALE, 1.20f).setDurationPush(100)
            .setDurationRelease(150).setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
            .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR).setOnClickListener {

//                    val intent = Intent()
//                    intent.setClassName("com.appsnado.haippNew", "com.appsnado.haippNew.MyBroadcastReceive")
//                    intent.action = "com.appsnado.haippNew.MyBroadcastReceive"
//                    intent.putExtra("KeyName", "code1id")
//                    sendBroadcast(intent)

                redview = false
                 if(preferenceManager!!.getchildlogout() == true){
                        logoutdailog()
                      }else{
                        toast("You cannot Logout Haipp",0)
                    }


            }





        if (preferenceManager!!.gettype() == "parent") {

            binding?.logout?.visibility = View.GONE
            binding?.task?.visibility = View.GONE
            binding?.feature?.visibility = View.VISIBLE
            binding?.setting?.visibility = View.VISIBLE

        } else {

            binding?.logout?.visibility = View.VISIBLE
            binding?.task?.visibility = View.VISIBLE
            binding?.feature?.visibility = View.GONE
            binding?.setting?.visibility = View.GONE

        }


        devicesid()









//Lock device






        dataset()
        not_detail = this
    }




    private fun dataset() {
        mDialogSearch = DialogSearch(this)
        val powerManager =
            this.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (powerManager != null && !powerManager.isIgnoringBatteryOptimizations(AppConstants.APP_PACKAGE_NAME)) {
                @SuppressLint("BatteryLife") val intent =
                    Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:" + AppConstants.APP_PACKAGE_NAME)
                startActivity(intent)
            }
        }
        if (!BackgroundManager.getInstance().init(this).isServiceRunning(LockService::class.java)) {
            BackgroundManager.getInstance().init(this).startService(LockService::class.java)
        }
        BackgroundManager.getInstance().init(this).startAlarmManager()



    }













    private fun devicesid() {

        devices_id = Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )

    }

    private fun setApplication(myRef: DatabaseReference) {

        // var list = getAppsToUpdate(myRef)
        Log.d(TAG, "setApplication: ")
        map.put("appdata", appdata!!)
        myRef.setValue(map)


    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleApiClient = GoogleApiClient.Builder(this@MainActivity)
            .addOnConnectionFailedListener(this)
            .addConnectionCallbacks(this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }

//
//    fun getAppsToUpdate(myRef: DatabaseReference): ArrayList<AppInfo> {
//        var myAppsToUpdate: ArrayList<AppInfo>? = null
//
//        val pm: PackageManager = this!!.getPackageManager()
//            val installedApps = pm.getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES or PackageManager.MATCH_DISABLED_COMPONENTS)
//        myAppsToUpdate = ArrayList<AppInfo>()
//        appdata = ArrayList<Model>()
//
//        for (aInfo in installedApps) {
//            if (aInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
//                // System apps
//            } else {
//                // Users apps
//                val app = Model()
//
//                val appInfo = AppInfo()
//                appInfo.setAppname(aInfo.loadLabel(pm).toString())
//                appInfo.setPacakagename(aInfo.packageName)
//                appInfo.setHide("0")
//
//                app.setAppname(aInfo.loadLabel(pm).toString())
//                app.setPacakagename(aInfo.packageName)
//                app.setHide("0")
//
//                appInfo.setLauncher(pm.getLaunchIntentForPackage(aInfo.packageName).toString())
//                try {
//                    val info = pm.getPackageInfo(aInfo.packageName, 0)
//                    // appInfo.setVersionName(info.versionName.toString())
//                    // appInfo.setVersionCode("" + info.versionCode)
//
//                    appInfo.setIcon(info.applicationInfo.loadIcon(pm))
//                    val drawable: Drawable = info.applicationInfo!!.loadIcon(pm)
//
//                    myAppsToUpdate!!.add(appInfo)
//                    appdata?.add(app)
//
//                } catch (e: PackageManager.NameNotFoundException) {
//                    Log.e("ERROR", "we could not get the user's apps")
//                }
//            }
//        }
//
//        return myAppsToUpdate!!
//    }

    fun logoutdailog() {


        val dialog1 = Dialog(this!!)
        dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1.setContentView(R.layout.dialog_logout)

        dialog1.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog1.show()
        val txtYes = dialog1.findViewById<MaterialButton>(R.id.btnAccept)
        val txtno = dialog1.findViewById<MaterialButton>(R.id.btnReject)

        txtYes.setOnClickListener {
            dialog1.dismiss()
            logoutfunction()
//            val i = Intent("com.appsnado.haippNew.Browser.browser.activity.BrowserActivity")
//            i.putExtra("KEY_DATA_EXTRA_FROM_ACTV_B", "145")
//            startActivity(i)


            // val pm = packageManager


        }

        txtno.setOnClickListener {
            dialog1.dismiss()


        }

    }

    private fun logoutfunction() {


        var userid = preferenceManager!!.getUser()?.userID
        mMainViewModel!!.loadingStatus!!.observe(this!!, LoadingObserver())
        mMainViewModel!!.userLiveData!!.observe(this!!, ResponcesObserver())
        mMainViewModel!!.loadDataNetwork(userid)
        cal_logout = true


        preferenceManager!!.setIsLogin(false)
        preferenceManager!!.putUser(null)
        WebServiceConstants.prefToken = ""
        preferenceManager!!.setToken("")
        preferenceManager!!.settype(null)
        preferenceManager!!.setdevicesid(null)
        preferenceManager!!.setlockscreen(false)
        preferenceManager!!.setstarttime(true)
        DbContract.devices.clear()


    }


    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(this@MainActivity) else UIHelper.hidedailog()
        }
    }

    private inner class ResponcesObserver : Observer<WebResponse<Any?>?> {
        override fun onChanged(reponces: WebResponse<Any?>?) {
            if (!cal_logout) return
            if (reponces == null) return
            cal_logout = false
            binding?.navigationViewTop?.visibility = View.GONE



            if (preferenceManager!!.gettype() == "parent") {
                // navController.popBackStack(R.id.settingFragment, true);
                //navController!!.navigate(R.id.loginFragment)
                navController.navigate(SettingFragmentDirections.actionSettingFragmentToLoginFragment())
            } else {
                if (navController.currentDestination?.id == R.id.homeFragment) {
                    navController.navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
                    // navController.navigateUp();
                    //   navController!!.navigate(R.id.loginFragment)

                } else if (navController.currentDestination?.id == R.id.chatFragment2) {

                    //   navController.navigateUp();
                    //   navController!!.navigate(R.id.loginFragment)
                    navController.navigate(ChatFragmentDirections.actionChatFragment2ToLoginFragment())
                } else {
                    try {
                        //   navController.navigate(TaskChildFragmentDirections.actionTaskChildFragmentToLoginFragment())
                        navController!!.navigate(R.id.taskChildFragment)
                        navController.navigate(TaskChildFragmentDirections.actionTaskChildFragmentToLoginFragment())


                        // navController.navigateUp();
                        //navController!!.navigate(R.id.loginFragment)
                    } catch (e: Exception) {
                        binding?.navigationViewTop?.visibility = View.VISIBLE
                        e.stackTrace
                    }
                }
            }
        }
    }


    companion object {
        private const val TAG = ""
        lateinit var navController: NavController

        var redview : Boolean = false
        var tokenfirebasedata: String? = ""

        @kotlin.jvm.JvmField
        var not_detail: MainActivity? = null
        fun getCallingIntent(_context: Context?): Intent {
            return Intent(_context, MainActivity::class.java)
        }


    }

    fun toast(message: String, i: Int) {
        if (i == 0) {
            UIHelper.showcustomsnackbar(this, message, binding!!.laytoat)
        } else {
            UIHelper.showsuccesscustomsnackbar(this, message, binding!!.laytoat)
        }

    }

    private fun permissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PermissionChecker.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) != PermissionChecker.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PermissionChecker.PERMISSION_GRANTED
            ) {
                fragmentManager.beginTransaction().addToBackStack(null).commit()
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION

                    ), 1337
                )
            }
        }


    }

    fun updateview(b: Boolean?) {
        if (b!!) {

            animation()
        }
    }

    private fun animation() {
        val animation = AnimationUtils.loadAnimation(
            this,
            com.appsnado.haippNew.R.anim.slide_in_from_bottom
        )
        binding?.navigationViewTop!!.startAnimation(animation)
        binding?.navigationViewTop?.visibility = View.VISIBLE

    }

    fun setView() {

        if (preferenceManager!!.gettype() == "parent") {
            binding?.logout?.visibility = View.GONE
            binding?.task?.visibility = View.GONE
            binding?.feature?.visibility = View.VISIBLE
            binding?.setting?.visibility = View.VISIBLE
        } else {
            binding?.logout?.visibility = View.VISIBLE
            binding?.task?.visibility = View.VISIBLE
            binding?.feature?.visibility = View.GONE
            binding?.setting?.visibility = View.GONE

        }


    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun loadAppInfoSuccess(list: MutableList<CommLockInfo>?) {
        Log.i(TAG, "loadAppInfoSuccess: " + list)
        val pm: PackageManager = this!!.getPackageManager()
        val myAppsToUpdate: java.util.ArrayList<com.appsnado.haippNew.Firebase.AppInfo>? = null
        val appdata: java.util.ArrayList<com.appsnado.haippNew.Firebase.Model>? = null
        Log.i("TAG", "loadAppInfoSuccess: $list")
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Apps")

    }

    override fun onClick(v: View?) {

    }

    private fun getDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this!!) { instanceIdResult ->
            tokenfirebasedata = instanceIdResult.getToken()
        }
    }

    private fun calltoken() {

        UIHelper.showDilog(this)
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    // Log.w(TAG, "getInstanceId failed", task.getException());
                    return@OnCompleteListener
                }
                UIHelper.hidedailog()
                // Get new Instance ID token
                tokenfirebasedata = task.result!!.token
                //  Log.i(TAG,  token);
                // Log and toast
            })


    }


    public fun  sendapi(userid: String?, appnname: String, texts: String, s: String) {

        val queue = Volley.newRequestQueue(this)
        val sr: StringRequest = object : StringRequest(
            Method.POST,
            "https://server.appsstaging.com/1461/haipp/public/api/post_notification",
            object : Response.Listener<String?> {
                override fun onResponse(response: String?) {
                    Log.i(TAG, "onResponse: "+response)

                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Log.i(TAG, "onResponse: "+error)

                }
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["notification_user_id"] = userid.toString()
                params["notification_title"] = appnname.toString()
                params["notification_message"] = texts.toString()
                params["notification_type"] = s.toString()

                return params
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/x-www-form-urlencoded"
                params["Authorization"] = "Bearer " + preferenceManager!!.getToken()
                return params
            }
        }
        queue.add(sr)

    }

    public fun sendNotification(notification: JSONObject) {
        val FCM_API = "https://fcm.googleapis.com/fcm/send"
        // TODO need to change serverkey
        val serverKey =
            "key=" + "AAAAsNXgU-M:APA91bFH4bFiNtth4YihTsDml0CjCwb7rgElelhKzgM-1ZEeug6NE2lrMyO4IEu0qQyT9XP6xaO8_g6h78sGnlAhRxgQ6EyYIdIcNIM5PRsccghmjHGZQAUcXaJml3hmU9ZJ5kUjXvWJ"
        val contentType = "application/json"
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener { response ->
                Log.i(
                    AppBlockerFragment.TAG,
                    "onResponse: $response"
                )
            },
            Response.ErrorListener {
                Toast.makeText(this, "Request error", Toast.LENGTH_LONG).show()
                Log.i(AppBlockerFragment.TAG, "onErrorResponse: Didn't work")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = java.util.HashMap()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        MySingleton.getInstance(FacebookSdk.getApplicationContext())
            .addToRequestQueue(jsonObjectRequest)
    }







    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
      //  if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                if(TaskDetailFragment.not_detail != null){
                    TaskDetailFragment.not_detail!!.calldata(result.uri)
                }

                // fileTemporaryProfilePicture = File(uri.path!!)
                //  uploadImageFile(fileTemporaryProfilePicture.getPath(), result.getUri().toString());
                //  setImageAfterResult(result.uri.toString())
                //  binding!!.ivcamera.visibility = View.GONE
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                error.printStackTrace()
            }
      //  }
    }

    fun chatfirebase(size: Int) {

        if(size > 0){
            binding!!.basketCount.visibility = View.VISIBLE
        }
       //

    }

    fun notification() {
        binding!!.basketCount.visibility = View.GONE
    }




}


