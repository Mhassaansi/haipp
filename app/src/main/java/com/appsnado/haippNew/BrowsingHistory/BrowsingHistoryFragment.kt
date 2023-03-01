package com.appsnado.haippNew.BrowsingHistory

import android.os.Bundle
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.AppBlockerActivity.AppBlockerFragment
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.BrowsingHistory
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BrowsingHistoryFragment : BaseFragment<BrowsingHistoryViewModel>() {
    var binding: BrowsingHistory? = null
    var browsingAdapter: BrowsingAdapter? = null
    var browsingArrayList: ArrayList<String>? = null

    var status: Boolean? = false

    companion object {
        fun newInstance() = BrowsingHistoryFragment()
    }

    private lateinit var browerhistviewModel: BrowsingHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.browsing_history_fragment,
            container,
            false
        )

        val currentDate = SimpleDateFormat("MMM d yyyy")
        val todayDate = Date()
        val thisDate: String = currentDate.format(todayDate)
        binding!!.datecurrent.text = thisDate
        setData()
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun createViewModel(): BrowsingHistoryViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        browerhistviewModel =
            ViewModelProviders.of(this, factory).get(BrowsingHistoryViewModel::class.java)
        return browerhistviewModel!!
    }


    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(baseActivity!!, "Browsing History")
    }

    override fun onDestroy() {
        status = false
        super.onDestroy()
    }

    fun setData() {

        status = true

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
        val myRefoneapp = myRefone.child("WebHistroy");
        val webref = myRefoneapp!!.child("Url")


        webref!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check for null
                if(status!!) {
                    if (dataSnapshot.value == null) {
                        Log.e(AppBlockerFragment.TAG, "User data is null!")

                        return
                    }

                    if (dataSnapshot.value != null) {
                        val t: GenericTypeIndicator<ArrayList<AddUrl>> =
                            object : GenericTypeIndicator<ArrayList<AddUrl>>() {}
                        val items: ArrayList<AddUrl> = dataSnapshot.getValue<ArrayList<AddUrl>>(t)!!


                        var dataSnapshot1: DataSnapshot
                        dataSnapshot.value



                        binding!!.rvBrowsingHistory.setLayoutManager(
                            LinearLayoutManager(
                                mainActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        )
                        browsingAdapter =
                            BrowsingAdapter(mainActivity!!, items)
                        binding!!.rvBrowsingHistory.adapter = browsingAdapter


                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e(AppBlockerFragment.TAG, "Failed to read user", error.toException())
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

//    fun setData() {
//
//        browsingArrayList = ArrayList()
//
//        browsingArrayList!!.add("Facebook")
//        browsingArrayList!!.add("Face Time")
//        browsingArrayList!!.add("Instagram")
//        browsingArrayList!!.add("LinkedIn")
//        browsingArrayList!!.add("Skype")
//        browsingArrayList!!.add("Messenger")
//        browsingArrayList!!.add("Snapchat")
//        browsingArrayList!!.add("Twitter")
//        browsingArrayList!!.add("WhatsApp")
//        browsingArrayList!!.add("Netflix")
//        browsingArrayList!!.add("LinkedIn")
//        browsingArrayList!!.add("Youtube")
//
//        binding!!.rvBrowsingHistory.setLayoutManager(
//            LinearLayoutManager(
//                mainActivity,
//                LinearLayoutManager.VERTICAL,
//                false
//            )
//        )
//        browsingAdapter =
//            BrowsingAdapter(mainActivity!!, browsingArrayList)
//        binding!!.rvBrowsingHistory.adapter = browsingAdapter
//    }
}
