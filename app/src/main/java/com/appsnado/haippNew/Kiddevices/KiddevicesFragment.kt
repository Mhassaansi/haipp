package com.appsnado.haippNew.Kiddevices

import android.app.Dialog
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.Adddevices.Adddevicesmodel
import com.appsnado.haippNew.Comman_Pacakges.data.DbContract
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.MainActivity
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.Kiddevices
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.retro.WebServiceConstants
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.*
import org.json.JSONException
import org.json.JSONObject

class KiddevicesFragment :  BaseFragment<KiddevicesViewModel>() {
    var binding: Kiddevices? = null
    var kidAdapter: KiddevicesAdapter? = null
    var kidsArrayList: ArrayList<Adddevicesmodel>? = null
    var cal_services : Boolean =false
    var add_services : Boolean =false
    private var childtokenref : DatabaseReference?=null

    private var tokenlistener: ValueEventListener? =null

    companion object {
        @kotlin.jvm.JvmField
        var not_detail: KiddevicesFragment? = null
        fun newInstance() = KiddevicesFragment()
    }

    private lateinit var viewModelkids: KiddevicesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.kiddevices_fragment,
            container,
            false
        )
       // startTopToBottomAnimation()
        setData()
        not_detail = this
        return binding!!.root;

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
    private fun startTopToBottomAnimation() {
//        val animation = AnimationUtils.loadAnimation(mainActivity, com.appsnado.haipp.R.anim.slide_in_from_top)
//        binding?.linearLayoutCompat2!!.startAnimation(animation)

    }

    fun setData() {

        viewModelkids!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        viewModelkids!!.userLiveData!!.observe(mainActivity!!,  ReObserver())
        viewModelkids!!.loadDataNetwork(mainActivity!!.preferenceManager!!.getUser()!!.userID!!)
        cal_services = true




//        kidAdapter =
//            KiddevicesAdapter(mainActivity!!, kidsArrayList)
//        binding!!.rvFeatures.adapter = kidAdapter
    }



    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()
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
            kidAdapter = KiddevicesAdapter(mainActivity!!, reponces)
            binding!!.rvFeatures.adapter = kidAdapter

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
    override fun createViewModel(): KiddevicesViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        viewModelkids = ViewModelProviders.of(this, factory).get(KiddevicesViewModel::class.java)
        return viewModelkids!!
    }

    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.txtTitleName?.text = "Kids Devices"
        titleBar?.btnLeft?.visibility = View.VISIBLE
        titleBar?.setBtnLeft(R.drawable.backbutton, View.OnClickListener {
            navController!!.popBackStack()
        });

    }

    fun detail(devicesid: String?, deviceTitle: String?) {
        logoutdailog(devicesid,deviceTitle)
    }



    fun logoutdailog(devicesid: String?, deviceTitle: String?) {


        val dialog1 = Dialog(mainActivity!!)
        dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1.setContentView(R.layout.dialog_logout)

        dialog1.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog1.show()
        val txtYes = dialog1.findViewById<MaterialButton>(R.id.btnAccept)
        val txtno = dialog1.findViewById<MaterialButton>(R.id.btnReject)


        val title = dialog1.findViewById<TextView>(R.id.tvTitle)
        val txt = dialog1.findViewById<TextView>(R.id.des)

        title.text ="Confirmation"
        txt.text ="Are you sure you want to Delete?"

        txtYes.setOnClickListener {
            dialog1.dismiss()
            UIHelper.showDilog(mainActivity)
            kidsArrayList = ArrayList()
            kidAdapter = KiddevicesAdapter(mainActivity!!, kidsArrayList)
            binding!!.rvFeatures.adapter = kidAdapter
            deletfirebase(devicesid!!,deviceTitle)
            //NotificationSend("true","updatejson","")
            viewModelkids!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
            viewModelkids!!.ReuserLiveData!!.observe(mainActivity!!,  AddObserver())
            add_services = true

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
                    NotificationSend("true","smartschedule","")
                    NotificationSend("true","ChildLogout","")
                    notificationset()
                    Log.e("TAG", "User data is null!")
                    viewModelkids!!.resend(devicesid!!)
                    if(DbContract.devices != null) {
                        DbContract.devices.clear()
                    }
                    UIHelper.hidedailog()

                }
                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    UIHelper.hidedailog()
                    Log.e("TAG", "Failed to read user", error.toException())
                }
            })



        }

        txtno.setOnClickListener {
            dialog1.dismiss()


        }

    }
    private fun notificationset() {
        var texts = ""
        val notification = JSONObject()
        val dataBody = JSONObject()
        val notificationBody = JSONObject()
        try{

            if(DbContract.childtoken != "") {
                dataBody.put("Allappscheck", "Allappscheck")
                dataBody.put("ischeck", "true")
                dataBody.put("bodymsg", texts)
                notificationBody.put("title", "Haipp")
                notificationBody.put("body", texts)
                notificationBody.put("click_action", "Haipp")
                notificationBody.put("notification_type", "Haipp")
                notification.put("to", DbContract.childtoken)
                notification.put("priority", "high")
                notification.put("data", dataBody)
                notification.put("notification", notificationBody)
                mainActivity?.sendNotification(notification)

            }else{
                Toast.makeText(mainActivity, "Please again select devices", Toast.LENGTH_LONG).show()
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }



    }

    private fun deletfirebase(devicesid: String, deviceTitle: String?) {
        val tvPN = ContactsContract.Directory.PACKAGE_NAME
        var usern = mainActivity?.preferenceManager?.getUser()?.userEmail
        val separated: List<String> = usern!!.split("@")
        var username = separated[0]
        separated[1]
      //  var deviceTitle = mainActivity?.preferenceManager?.getdevicestitle()
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
        val ChildLogout = myRefone.child("ChildLogout");
        childtokenref = myRefone.child("token");
        val installmyRef = myRefone.child("installpermission");
        installmyRef.setValue(true)
        fierbasetoken.setValue("")
        ChildLogout.setValue(true)

    }









    fun NotificationSend(ischeck: String, appnname: String,json: String) {
        var texts = ""
        val notification = JSONObject()
        val dataBody = JSONObject()
        val notificationBody = JSONObject()
        try {

            if(DbContract.childtoken != "") {
                texts = "Your smart schedule is updated"

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
            }else{
                Toast.makeText(mainActivity,"Please again select devices", Toast.LENGTH_LONG).show()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}