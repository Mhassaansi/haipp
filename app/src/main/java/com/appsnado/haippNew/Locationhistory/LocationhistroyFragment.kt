package com.appsnado.haippNew.Locationhistory

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.LocationHistory
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class LocationhistroyFragment : BaseFragment<LocationhistroyViewModel>(), OnMapReadyCallback {
    var binding: LocationHistory? = null
    var locationAdapter: LocationhistroAdapter? = null
    var browsingArrayList: ArrayList<String>? = null
    var map: GoogleMap? = null
    var add_services : Boolean =false

    var simpleDateOnlyFormat: SimpleDateFormat? = null
    companion object {
        fun newInstance() = LocationhistroyFragment()
    }

    private lateinit var locationhistviewModel: LocationhistroyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.locationhistroy_fragment, container, false
        )
       // simpleDateFormat = SimpleDateFormat("EEE d MMM HH:mm", Locale.getDefault())
      //  this.simpleTimeFormat = SimpleDateFormat("hh:mm aa", Locale.getDefault())
        simpleDateOnlyFormat = SimpleDateFormat("MMM d yyyy", Locale.getDefault())
      //  this.simpleDateLocaleFormat = SimpleDateFormat("EEE d MMM", Locale.GERMAN)
        binding!!.mapView.onCreate(savedInstanceState);
        binding!!.mapView.getMapAsync(this);
        setData()

        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun createViewModel(): LocationhistroyViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        locationhistviewModel =
            ViewModelProviders.of(this, factory).get(LocationhistroyViewModel::class.java)
        return locationhistviewModel!!
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding?.mapView?.onSaveInstanceState(outState);
    }

    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(baseActivity!!, "Location History")
    }


    fun setData() {
        var da :String? = null
        binding?.date?.setOnClickListener {
//            SingleDateAndTimePickerDialog.Builder(context)
//                .backgroundColor(resources.getColor(R.color.colortitlebarbg))
//                .mainColor(Color.GREEN)
//                .titleTextColor(resources.getColor(R.color.white))
//                .display()
            SingleDateAndTimePickerDialog.Builder(context)
                .backgroundColor(resources.getColor(R.color.colortitlebarbg))
                .mainColor(Color.GREEN)
                .bottomSheet()
                .curved()
                .displayMinutes(false)
                .displayHours(false)
                .displayDays(false)
                .displayMonth(true)
                .displayYears(true)
                .displayDaysOfMonth(true)
                .titleTextColor(resources.getColor(R.color.white))
                .listener(SingleDateAndTimePickerDialog.Listener {

                   date ->  da


                   binding?.setdate?.text = simpleDateOnlyFormat?.format(date)


                    try {

                       var datestring = parseDateToddMMyyyy(binding?.setdate?.text.toString())
                        locationhistviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
                        locationhistviewModel!!.userLiveData!!.observe(mainActivity!!,  ReObserver())
                        locationhistviewModel!!.loadDataNetwork(mainActivity!!.preferenceManager?.getdevicesid(), datestring.toString())
                        add_services= true

                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }




                })

                 .display()
        }

//        browsingArrayList = ArrayList()
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

//        binding!!.rvlocationHistory.setLayoutManager(
//            LinearLayoutManager(
//                mainActivity,
//                LinearLayoutManager.VERTICAL,
//                false
//            )
//        )
//        locationAdapter =
//            LocationhistroAdapter(mainActivity!!, browsingArrayList)
//        binding!!.rvlocationHistory.adapter = locationAdapter

    }


    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()
        }
    }


    fun parseDateToddMMyyyy(time: String): String? {
        val outputPattern = "yyyy-MM-dd"
        val inputPattern = "MMM d yyyy"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

    private inner class ReObserver : Observer<java.util.ArrayList<locationhistroyModel>> {
        override fun onChanged(reponces: java.util.ArrayList<locationhistroyModel>?) {
            if(!add_services) return
            if (reponces == null) return
            add_services = false

            binding!!.rvlocationHistory.setLayoutManager(LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false))
            locationAdapter = LocationhistroAdapter(mainActivity!!)
            binding!!.rvlocationHistory.adapter = locationAdapter

            locationAdapter!!.addAll(reponces)
            locationAdapter!!.notifyDataSetChanged()
        }
    }





    override fun onMapReady(p0: GoogleMap?) {
        map = p0;
        map!!.getUiSettings().setMyLocationButtonEnabled(false);
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


        map!!.setMyLocationEnabled(true);
        // map!!.setOnCameraChangeListener(this)


    }
}