package com.appsnado.haippNew.Activityreport

import android.R
import android.os.Bundle
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.AppBlockerActivity.AppBlockerFragment
import com.appsnado.haippNew.Applocakpacakges.base.AppConstants.mTotal
import com.appsnado.haippNew.Firebase.Model
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.Monitorapp.data.AppItem
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.Reportbind
import com.appsnado.haippNew.retro.WebServiceConstants
import com.google.firebase.database.*

class ReportFragment : BaseFragment<ReportViewModel>(), AdapterView.OnItemSelectedListener,
    View.OnClickListener {
    private var order: Map<String, ArrayList<Model>> = HashMap()
    private var selectedItems: ArrayList<Model>? = null
    var  binding : Reportbind? =null
    var arraySpinner = arrayOf<String?>("Daily", "Weekly", "Monthly", "Yearly")
    private  var selectAdapter: ArrayAdapter<*>? = null

    var appreportAdapter: ReportAdapter? = null
    var myadapterlist: MyAdapter? = null
    var appBlockerArrayList: ArrayList<String>? = null
    var myRefoneapp : DatabaseReference?=null
    var listener: ValueEventListener? =null
    companion object {
        fun newInstance() = ReportFragment()
    }

    private lateinit var reportrviewModel: ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.report_fragment,
            container,
            false
        )

        binding!!.selctvalue.setOnClickListener {  binding?.select?.performClick() }
        binding!!.select.onItemSelectedListener = this
        selectAdapter =
            ArrayAdapter<String?>(mainActivity!!, R.layout.simple_spinner_item, arraySpinner)
        selectAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.select.adapter = selectAdapter

       // setData()
        setdataview()




        return binding!!.root
    }

    private fun setdataview() {
        val tvPN = PACKAGE_NAME
        var usern = mainActivity?.preferenceManager?.getUser()?.userEmail
        val separated: List<String> = usern!!.split("@")
        var username = separated[0]
        separated[1]
        var deviceTitle= mainActivity?.preferenceManager?.getdevicestitle()
        var devicesid =  mainActivity?.preferenceManager?.getdevicesid()
        val database = FirebaseDatabase.getInstance()
        val ui: Double = WebServiceConstants.userid!!.toDouble()
        val i = ui!!.toInt()
        val vi: Double = devicesid!!.toDouble()
        val v = vi!!.toInt()
        val myRef1 = database.getReference("Devicesdata")
        val myRefoneparent = myRef1.child("Parent_"+username+"_"+i.toString())
        val Childdevices = myRefoneparent.child("Childdevices")
        val myRefone = Childdevices.child("Child_"+deviceTitle +"_" +v.toString())
        myRefoneapp = myRefone.child("AppsMonitor");
        listener = myRefoneapp!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check for null
                if (dataSnapshot.value == null) {
                    Log.e(AppBlockerFragment.TAG, "User data is null!")

                    return
                }


                val t: GenericTypeIndicator<ArrayList<AppItem>> =
                        object : GenericTypeIndicator<ArrayList<AppItem>>() {}
                val items: ArrayList<AppItem> = dataSnapshot.getValue<ArrayList<AppItem>>(t)!!


                mTotal = 0
                for (item in items) {
                    if (item.mUsageTime <= 0) continue
                    mTotal += item.mUsageTime

                }

                binding!!.rvreport.setLayoutManager(
                        LinearLayoutManager(
                                mainActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                        )
                )
                myadapterlist = MyAdapter(
                        mainActivity,
                        items
                )
                binding!!.rvreport.adapter = myadapterlist
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e(AppBlockerFragment.TAG, "Failed to read user", error.toException())
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun createViewModel(): ReportViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        reportrviewModel = ViewModelProviders.of(this, factory).get(ReportViewModel::class.java)
        return reportrviewModel!!
    }


    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(baseActivity!!, "Activity Report")
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

        val ui: Double = WebServiceConstants.userid!!.toDouble()
        val i = ui!!.toInt()
        val vi: Double = devicesid!!.toDouble()
        val v = vi!!.toInt()

        val myRef1 = database.getReference("Devicesdata")
        val myRefoneparent = myRef1.child("Parent_"+username+"_"+i.toString())
        val Childdevices = myRefoneparent.child("Childdevices")
        val myRefone = Childdevices.child("Child_"+deviceTitle +"_" +v.toString())
         myRefoneapp = myRefone.child("Apps_usage");
        // val myRef = myRefoneapp.child("Apps");

        // DatabaseReference myRef = database.getReference("Apps");
        // DatabaseReference myRef = database.getReference("Apps");


       listener = myRefoneapp!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check for null
                if (dataSnapshot.value == null) {
                    Log.e(AppBlockerFragment.TAG, "User data is null!")

                    return
                }
                order = dataSnapshot.value as Map<String, ArrayList<Model>>
                selectedItems = ArrayList(order.get("usage_app"))
                Log.i(AppBlockerFragment.TAG, "onDataChange: $selectedItems")
               // finalAppdata = selectedItems

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






                binding!!.rvreport.setLayoutManager(
                        LinearLayoutManager(
                                mainActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                        )
                )
                appreportAdapter = ReportAdapter(
                        mainActivity!!,
                        selectedItems
                )
                binding!!.rvreport.adapter = appreportAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e(AppBlockerFragment.TAG, "Failed to read user", error.toException())
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        myRefoneapp?.removeEventListener(listener!!)

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        if (spinnerCheck ++ > 0) {
//            binding!!.selctvalue.text =
//
//        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

        }
    }


}

