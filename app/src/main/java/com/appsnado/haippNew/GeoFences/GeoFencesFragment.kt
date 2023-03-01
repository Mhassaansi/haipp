package com.appsnado.haippNew.GeoFences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.appsnado.haippNew.databinding.GeoFences
import com.appsnado.haippNew.retro.WebResponse

class GeoFencesFragment : BaseFragment<GeoFencesViewModel>(),View.OnClickListener {
    var  binding : GeoFences? =null
    var geoFencesAdapter: GeoFencesAdapter? = null
    var geoFenceArrayList: ArrayList<String>? = null
    var cal_services : Boolean =false
    var add_services : Boolean =false
    var geoDialog: GeoFencesDialog? = null
   // // var geoFencesDialog: GeoFencesDialog? = null
    companion object {
       @kotlin.jvm.JvmField
        var not_detail: GeoFencesFragment? = null
        fun newInstance() = GeoFencesFragment()
    }
    private lateinit var geofencesviewModel: GeoFencesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
          binding =  DataBindingUtil.inflate(inflater,R.layout.geo_fences_fragment, container, false)
          setData()

          not_detail = this
          return binding!!.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }


    override fun createViewModel(): GeoFencesViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        geofencesviewModel = ViewModelProviders.of(this, factory).get(GeoFencesViewModel::class.java)
        return geofencesviewModel!!
    }

    fun  setData() {
        binding!!.btnAdd.setOnClickListener(this)

       if(mainActivity?.preferenceManager?.gettype() == "parent") {
           geofencesviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
           geofencesviewModel!!.userLiveData!!.observe(mainActivity!!, ReObserver())
           geofencesviewModel!!.Alldata(mainActivity?.preferenceManager?.getdevicesid()!!)
           cal_services = true
       }


    }


    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(baseActivity!!, "Geo Fences")

    }



    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> {



                geoDialog = GeoFencesDialog(mainActivity!!, "Email")
                mainActivity!!.supportFragmentManager?.let {
                    geoDialog!!.show(
                            it,
                            null
                    )
                }



            }
        }
    }

    fun call(name: String, placename: String?, lat: String, lon: String, range: String) {

        geofencesviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        geofencesviewModel!!.ReuserLiveData!!.observe(mainActivity!!,  AddObserver())
        geofencesviewModel!!.loadDataNetwork(mainActivity?.preferenceManager?.getdevicesid()!!, name,placename!!,lat,lon,range.toString())
        add_services= true

//        geoFenceArrayList!!.add("")
//        geoFencesAdapter!!.addAll(geoFenceArrayList!!)
//        geoFencesAdapter!!.notifyDataSetChanged()
    }

    fun detail(dcId: String?) {
        geofencesviewModel!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        geofencesviewModel!!.ReuserLiveData!!.observe(mainActivity!!,  AddObserver())
        geofencesviewModel!!.DeletGeo(dcId!!)
        add_services= true
    }

    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()
        }
    }

    private inner class ReObserver : Observer<java.util.ArrayList<GeoFencesModel>> {
        override fun onChanged(reponces: java.util.ArrayList<GeoFencesModel>?) {
            if(!cal_services) return
             if (reponces == null) return
                 cal_services = false

                binding!!.rvGeoFence.setLayoutManager(LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false))
                geoFencesAdapter = GeoFencesAdapter(mainActivity!!)
                binding!!.rvGeoFence.adapter = geoFencesAdapter

                geoFencesAdapter!!.addAll(reponces!!)
                geoFencesAdapter!!.notifyDataSetChanged()
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

}