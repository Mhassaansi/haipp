package com.appsnado.haippNew.GeoFences

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.appsnado.haippNew.Activities.MainActivity
import com.appsnado.haippNew.R
import com.appsnado.haippNew.databinding.GeoFence
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.theartofdev.edmodo.cropper.CropImage
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import java.util.*


class GeoFencesDialog(var mainActivity: MainActivity, s: String) : DialogFragment(), View.OnClickListener {

    var binding: GeoFence? = null
    private  val AUTOCOMPLETE_REQUEST_CODE_FOR_CLUB_ADDRESS = 1
    var  constant : Context? = null
    private var lon = "0"
    private var lat = "0"
    private var rangevalue = ""

    private var Placename: String? = null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.geo_fences_dialog, container, false)
        initPlacesApi()
        binding!!.tvAccept.setOnClickListener(this)
        binding!!.tvReject.setOnClickListener(this)
        binding!!.location.setOnClickListener(this)


        binding!!.valrange.setOnSeekChangeListener(object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
                rangevalue = seekParams.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {}
        })
        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme)
        isCancelable = true
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvAccept -> {

                  if(binding!!.name.text.toString() != "" && Placename != null) {
                      if (GeoFencesFragment.not_detail != null) {
                          GeoFencesFragment.not_detail!!.call(
                              binding!!.name.text.toString(),
                              Placename,
                              lat,
                              lon,
                              rangevalue
                          )
                      }
                  }else{
                      mainActivity.toast("Please Fill TextFeilds",0);
                  }
                dismiss()
            }

            R.id.tvReject -> {
                dismiss()
            }
            R.id.location -> {
                openAutoComplete(AUTOCOMPLETE_REQUEST_CODE_FOR_CLUB_ADDRESS)
            }
        }
    }
    private fun initPlacesApi() {
        val apiKey = getString(R.string.api_key)
        if (!Places.isInitialized()) {
            Places.initialize(mainActivity!!.applicationContext, apiKey)
        }
        // placesClient = Places.createClient(getMainActivity());
    }
    private fun openAutoComplete(a: Int) {
        val fields: List<Place.Field> = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG)
        if (context != null) {
            val intent = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(mainActivity)
            startActivityForResult(intent, a)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE_FOR_CLUB_ADDRESS) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val place: Place = Autocomplete.getPlaceFromIntent(data)
                    Log.i("place123", "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress())
                    //updateUIForClubAddress(place);
                     Placename =  place.getName() + "," + place.getAddress()
                     binding!!.location.setText(place.getName() + "," + place.getAddress())
                     val plas: LatLng = place.getLatLng()!!
                        lat = plas.latitude.toString()
                        lon = plas.longitude.toString()

                }
            }
        }
    }


}