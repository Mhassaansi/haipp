package com.appsnado.haippNew.taskChild

import android.os.Bundle
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.AppBlockerActivity.AppBlockerFragment
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.Claim
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.taskParent.*
import com.google.firebase.database.*

class ClaimFragment : BaseFragment<ClaimViewModel>(), View.OnClickListener {
    var binding: Claim? = null
    var taskAdapter: ClaimAdapter? = null
    var taskArrayList: ArrayList<String>? = null
    var Dialog: ClaimDailogbox? = null
    var cal_services : Boolean =false
    var add_services : Boolean =false
    var calpoint :Int = 0
    var childclaim:DatabaseReference? = null
    var usagepoint :Int = 0
    var calculatepoint : Boolean =false


    var calculatepointvalue : Int = 0
    private var listenerdata: ValueEventListener? = null

    companion object {
        @kotlin.jvm.JvmField
        var not_detail: ClaimFragment? = null
        fun newInstance() = ClaimFragment()

    }

    private lateinit var viewModelfeature: ClaimViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.fragment_claim,
            container,
            false
        )
        startTopToBottomAnimation()
        setData()
        not_detail = this
        return binding!!.root;

    }

    private fun startTopToBottomAnimation() {


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun setData() {

        taskArrayList = ArrayList()


        viewModelfeature!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        viewModelfeature!!.userLiveData!!.observe(mainActivity!!, ReObserver())
        viewModelfeature!!.loadDataNetwork(mainActivity?.preferenceManager?.getdevicesid()!!)
        cal_services = true




    }

    override fun createViewModel(): ClaimViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        viewModelfeature =
            ViewModelProviders.of(this, factory).get(ClaimViewModel::class.java)
        return viewModelfeature
    }

    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(baseActivity!!, "Claim Points")
    }

    override fun onClick(v: View?) {

    }

    fun calldailog(tsId: String?, taskReward: String?, con: String?) {
        Dialog = ClaimDailogbox(mainActivity!!, "Email",tsId,taskReward,con)
        mainActivity!!.supportFragmentManager?.let {
            Dialog!!.show(
                    it,
                    null
            )
        }
    }

    fun calldata(taskid: String, taskward: String, con: String) {
        viewModelfeature!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        viewModelfeature!!.ReuserLiveData!!.observe(mainActivity!!, AddObserver())
        viewModelfeature!!.claimtask(taskid,taskward,con)
        add_services = true
    }


    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()
        }
    }



    private inner class ReObserver : Observer<java.util.ArrayList<claimModel>> {
        override fun onChanged(reponces: java.util.ArrayList<claimModel>?) {
            if(!cal_services) return
            if (reponces == null) return
            cal_services = false
            calpoint = 0
            binding!!.rvClaim.layoutManager = LinearLayoutManager(
                    mainActivity,
                    LinearLayoutManager.VERTICAL,
                    false
            )

            taskAdapter =
                    ClaimAdapter(mainActivity!!, reponces)
            binding!!.rvClaim.adapter = taskAdapter
            taskAdapter!!.notifyDataSetChanged()

            for (item in reponces!!) {

                if(item.reward != null){
                   var cal = item.task_reward!!.toInt()
                   calpoint  = cal + calpoint
                }
            }
                    if(calculatepoint){
                        var p = calpoint - usagepoint
                        calpoint = p
                        childclaim!!.setValue(calpoint)
                        binding!!.totalpoint.text = calpoint.toString()
                     }else{
                         pointfirebase(calpoint)
                    }



        }
    }

    private fun pointfirebase(point: Int) {
        calculatepoint = false
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
        val vi: Double = mainActivity?.preferenceManager!!.getdevicesid()!!.toDouble()
        val v = vi!!.toInt()

        val myRef1 = database.getReference("Devicesdata")
        val myRefoneparent = myRef1.child("Parent_" + username + "_" + i.toString())
        val Childdevices = myRefoneparent.child("Childdevices")
        val myRefone = Childdevices.child("Child_" + deviceTitle + "_" + v.toString())
         childclaim = myRefone.child("claimpoint");
        if (mainActivity!!.preferenceManager!!.getstarttime()!!){

            mainActivity?.toast("First Stop Time",0)
          ///  Toast.makeText(mainActivity,"First Stop Time", Toast.LENGTH_LONG).show()
           // pointfirebase(calpoint)
            //childclaim.setValue(point)
           }else{

           // Toast.makeText(mainActivity,"First Stop Time", Toast.LENGTH_LONG).show()
            gettimevissible(childclaim!!,point)
        }

    }

    private fun gettimevissible(childclaim: DatabaseReference, point: Int) {
        listenerdata = childclaim!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check for null
                if (dataSnapshot.value == null) {
                    return
                 }
                var time = dataSnapshot.value.toString()
                if (time != "0") {
                    childclaim?.removeEventListener(listenerdata!!)
                    if(point >= time.toLong()){

                        calculatepoint = true
                        usagepoint = point - time.toInt()
                        binding!!.totalpoint.text = time.toString()
                        calculatepointvalue = time.toInt()
                     }
                } else {
                    childclaim?.removeEventListener(listenerdata!!)
                }


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e(AppBlockerFragment.TAG, "Failed to read user", error.toException())
            }
        })
    }

    private inner class AddObserver : Observer<WebResponse<Any?>?> {
        override fun onChanged(reponces: WebResponse<Any?>?) {
            if(!add_services) return
            if (reponces == null) return
             add_services = false
             calculatepoint = true
             mainActivity?.toast(reponces.message!!,1)
            setData()

        }
    }
}