package com.appsnado.haippNew.Feature
import android.os.Bundle
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.Comman_Pacakges.data.DbContract
import com.appsnado.haippNew.Comman_Pacakges.data.DbContract.Companion.newsItems
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.Features
import com.appsnado.haippNew.retro.WebServiceConstants.userid
import com.google.firebase.database.*
import org.json.JSONException
import org.json.JSONObject

class FeatureFragment :  BaseFragment<FeatureViewModel>(), AdapterView.OnItemSelectedListener,
    View.OnClickListener {
    var binding: Features? = null
    var featureAdapter: FeatureAdapter? = null
    var featuresArrayList: ArrayList<String>? = null
    var spinnerCheck = 0
    var selectAdapter: ArrayAdapter<*>? = null


    private var installmyRef : DatabaseReference?=null
    private var ChildLogout : DatabaseReference?=null
    private var lockmyRef : DatabaseReference?=null
    private var childtokenref : DatabaseReference?=null
    private var tokenlistener: ValueEventListener? =null
    private var installlistener: ValueEventListener? =null
    private var locklistener: ValueEventListener? =null

    private var ChildLogoutlistener: ValueEventListener? =null

    private var devicesid :String? = null
    private var devicetittle :String? = null

    var arraySpinner =
        arrayOf<String?>("Select Device", "Pixel", "Nexus", "Samsung")

    companion object {
        @kotlin.jvm.JvmField
        var not_detail: FeatureFragment? = null
        var selectchild :Boolean = false
        fun newInstance() = FeatureFragment()
    }

    private lateinit var viewModelfeature: FeatureViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.feature_fragment,
            container,
            false
        )
       /// startTopToBottomAnimation()
        selectchild = false

        setData()
        not_detail = this
        return binding!!.root;

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
    }

    private fun setupSpinner() {
        binding!!.device.setOnClickListener(this)
        binding!!.select.onItemSelectedListener = this
//        selectAdapter = ArrayAdapter<Any?>(mainActivity!!, android.R.layout.simple_spinner_item, arraySpinner)
//        selectAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding!!.select.adapter = selectAdapter


        selectAdapter = ArrayAdapter<String?>(mainActivity!!, android.R.layout.simple_spinner_item, DbContract.devices)
        selectAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.select.adapter = selectAdapter
    }
    private fun startTopToBottomAnimation() {
        val animation = AnimationUtils.loadAnimation(mainActivity, com.appsnado.haippNew.R.anim.slide_in_from_top)
        binding?.linearLayoutCompat2!!.startAnimation(animation)

    }

    fun setData() {

        featuresArrayList = ArrayList()
        featuresArrayList!!.add("Activity Report")
        featuresArrayList!!.add("App Blocker")
        featuresArrayList!!.add("Browsing History")
        featuresArrayList!!.add("Geo Fences")
        featuresArrayList!!.add("Location History")
        featuresArrayList!!.add("Smart Schedule")
        featuresArrayList!!.add("Task")
        featuresArrayList!!.add("Work Sheet")
//        featuresArrayList!!.add("Web Filters")
        featuresArrayList!!.add("Uninstall Permission")
        featuresArrayList!!.add("Lock Mobile")
        featuresArrayList!!.add("Child Logout Permission")

        binding!!.rvFeatures.layoutManager = LinearLayoutManager(
            mainActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        featureAdapter =
            FeatureAdapter(mainActivity!!, featuresArrayList)
        binding!!.rvFeatures.adapter = featureAdapter


    }

    private fun firebase(devicesid: String?, deviceTitle: String?) {
          UIHelper.showprogress(mainActivity)
        val tvPN = PACKAGE_NAME
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
        val myRefoneparent = myRef1.child("Parent_"+username+"_"+i.toString())
        val Childdevices = myRefoneparent.child("Childdevices")
        val myRefone = Childdevices.child("Child_"+deviceTitle +"_" +v.toString())
        installmyRef = myRefone.child("installpermission");
        lockmyRef = myRefone.child("Phonelock");
        childtokenref = myRefone.child("token");
        ChildLogout = myRefone.child("ChildLogout");


        installlistener = installmyRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check for null
                if (dataSnapshot.value == null) {
                    Log.e("TAG", "User data is null!")
                    UIHelper.hidedailog()
                    DbContract.install = ""
                    return
                }
                DbContract.install = dataSnapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e("TAG", "Failed to read user", error.toException())
            }
        })

        locklistener = lockmyRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check for null
                if (dataSnapshot.value == null) {
                    Log.e("TAG", "User data is null!")
                    UIHelper.hidedailog()
                    DbContract.install = ""
                    return
                }
                DbContract.lockvalue = dataSnapshot.value.toString()

                featureAdapter?.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e("TAG", "Failed to read user", error.toException())
            }
        })

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

        ChildLogoutlistener = ChildLogout!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check for null
                if (dataSnapshot.value == null) {
                    Log.e("TAG", "User data is null!")
                    DbContract.install = ""
                    UIHelper.hidedailog()
                    return
                }
                DbContract.logoutpermission = dataSnapshot.value.toString()
                UIHelper.hidedailog()
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e("TAG", "Failed to read user", error.toException())
            }
        })


    }
    override fun onDestroy() {
        super.onDestroy()
        if (locklistener!= null){
            lockmyRef?.removeEventListener(locklistener!!)
            installmyRef?.removeEventListener(installlistener!!)
            childtokenref?.removeEventListener(tokenlistener!!)
            ChildLogout?.removeEventListener(ChildLogoutlistener!!)

        }
    }

    override fun onResume() {
        super.onResume()
        if (locklistener!= null){
            lockmyRef?.removeEventListener(locklistener!!)
            installmyRef?.removeEventListener(installlistener!!)
        }

    }
    override fun createViewModel(): FeatureViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        viewModelfeature = ViewModelProviders.of(this, factory).get(FeatureViewModel::class.java)
        return viewModelfeature
    }

    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.text("Feature")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (spinnerCheck++ > 0) {
            binding!!.device.text = newsItems[position].device_title
            if(mainActivity?.preferenceManager!!.gettype() == "parent"){

                selectchild = true
                mainActivity?.preferenceManager!!.setdevicesid( newsItems[position].devicesid)
                mainActivity?.preferenceManager!!.setdevicetitle( newsItems[position].device_title)
                devicesid = newsItems[position].devicesid
                devicetittle = newsItems[position].device_title
                firebase(devicesid,devicetittle)

            }
        }
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.device -> binding!!.select.performClick()
        }
    }

    fun datafirebase(value: Int, check:Boolean) {
        if(value == 1){
            installmyRef?.setValue(check)
            var texts = "Parent Update Haipp Application Install Permission "
            NotificationSend(check.toString(),"Installpermission",texts)
        }else if(value == 2){
            ChildLogout?.setValue(check)
            var texts = "Parent Update Haipp Application Logout Permission "
            NotificationSend(check.toString(),"ChildLogout",texts)
        }else {
            lockmyRef?.setValue(check)
            var texts = "Parent Update Haipp Application Mobile Lock Permission "
            NotificationSend(check.toString(),"Lockpermission",texts)

        }
    }
    private fun NotificationSend(ischeck: String, appnname: String, texts: String) {
        val notification = JSONObject()
        val dataBody = JSONObject()
        val notificationBody = JSONObject()
        try {

//            if(ischeck.equals("true")){
//                texts = "Parent App Lock Haipp Application Permission "
//            }else{
//                texts = "Parent App Unlock Haipp Application Permission"
//            }

            if(DbContract.childtoken != "") {

            dataBody.put(appnname,appnname)
            dataBody.put("ischeck",ischeck)
            dataBody.put("bodymsg",texts)
            notificationBody.put("title", "Haipp")
            notificationBody.put("body", "Haipp")
            notificationBody.put("click_action", "Haipp")
            notificationBody.put("notification_type", "Haipp")
            notification.put("to",  DbContract.childtoken)
            notification.put("priority", "high")
            notification.put("data", dataBody)
            notification.put("notification", notificationBody)
            mainActivity?.sendNotification(notification)

            }else{
                Toast.makeText(mainActivity,"Please again select devices", Toast.LENGTH_LONG).show()
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


}