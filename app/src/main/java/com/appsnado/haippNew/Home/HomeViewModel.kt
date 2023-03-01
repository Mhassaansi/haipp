package com.appsnado.haippNew

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.appnado.haipp.baseviewmodel.BaseViewModel
import com.appsnado.haippNew.Adddevices.Adddevicesmodel
import com.appsnado.haippNew.Comman_Pacakges.data.UserWrapper
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Home.LastModel
import com.appsnado.haippNew.data.SharedPreferenceManager
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.retro.WebServiceConstants
import com.appsnado.haippNew.retro.WebServiceFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeViewModel(services: WebServiceFactory, activity: Activity?) : BaseViewModel<Any>() {
    private val webservices: WebServiceFactory
    var preferenceManag: SharedPreferenceManager? = null
    val userLiveData: MutableLiveData<ArrayList<Adddevicesmodel>>
    val ReuserLiveData: MutableLiveData<WebResponse<Any?>?>
    val lastlocation: MutableLiveData<LastModel>

    val loadingStatus: MutableLiveData<Boolean>
    private var user: UserWrapper
    private var act: Activity? = null


    init {
        user = UserWrapper()
        webservices = services;
        act = activity
        loadingStatus = MutableLiveData()
        userLiveData = MutableLiveData()
        ReuserLiveData = MutableLiveData()
        lastlocation = MutableLiveData()
        preferenceManag = SharedPreferenceManager(act)

    }

    fun loadDataNetwork(code: String) {
        setIsLoading(true)
        webservices!!.getservices()!!.getdevices(WebServiceConstants.userid)!!.enqueue(ReResponcesCallback())
    }



    fun loadloacation(code: String) {
        setIsLoading(true)
        webservices!!.getservices()!!.getlocation(code)!!.enqueue(LastlocationCallback())
    }


    fun getuserLiveData(): MutableLiveData<ArrayList<Adddevicesmodel>> {
        return userLiveData
    }

    private fun setUserLiveData(use: ArrayList<Adddevicesmodel>) {
        setIsLoading(false)
        userLiveData.postValue(use)
    }

    private fun setReUserLiveData(use: WebResponse<Any?>?) {
        setIsLoading(false)
        ReuserLiveData.postValue(use)
    }

    private fun setIsLoading(loading: Boolean) {
        loadingStatus.postValue(loading)
    }

    private fun createRequestBodyString(text: String?): RequestBody? {
        return if (text != null) {
            RequestBody.create(MultipartBody.FORM, text)
        } else null
    }

    fun resend(devicesid: String) {
        setIsLoading(true)
        webservices!!.getservices()!!.devicedelete(devicesid)!!.enqueue(AddCallback())
    }

    private fun setlastlocation(use: LastModel) {
        setIsLoading(false)
        lastlocation.postValue(use)
    }

    private inner class LastlocationCallback : Callback<WebResponse<Any?>?> {
        override fun onResponse(call: Call<WebResponse<Any?>?>, response: Response<WebResponse<Any?>?>) {
            if (webservices != null) {
                var api_responces: Boolean = UIHelper.validateIfWebResponse(response, act)
                if (api_responces) {

                    val gson = Gson()
                    val strJson = gson.toJson(response.body()?.data )
                    var item = getlocation(strJson)

                    setlastlocation(item)
                }
                setIsLoading(false)
            }
        }

        override fun onFailure(call: Call<WebResponse<Any?>?>, t: Throwable) {
            t.cause
            setIsLoading(false)
        }
    }


    private inner class ReResponcesCallback : Callback<WebResponse<Any?>?> {
        override fun onResponse(call: Call<WebResponse<Any?>?>, response: Response<WebResponse<Any?>?>) {
            if (webservices != null) {
                var api_responces: Boolean = UIHelper.validateIfWebResponse(response, act)
                if (api_responces) {
//                    val gson = GsonBuilder()
//                            .registerTypeAdapterFactory(LengthArrayTypeAdapterFactory.getLengthArrayTypeAdapterFactory())
//                            .create()
                   // val myJsonData = gson.toJson(response.body()?.data)
//                    val gson = Gson()
//                    val s2: String = gson.toJson(response.body()?.data)
//                    val getLayoutResponseMapper: Genericdatapass= Gson().fromJson(s2, Genericdatapass::class.java)

                      val gson = Gson()
                      //Convert to json data and save it
                      //Convert to json data and save it
                      val strJson = gson.toJson(response.body()?.data )
                      var item = getDataList(strJson)
                      var newsItems = ArrayList<Adddevicesmodel>()
                      //val elementsResponse: WebResponse<List<Adddevicesmodel?>?>? = gson.fromJson(myJsonData, Adddevicesmodel::class.java)
                      ///newsItems = response.body()?.data as ArrayList<Adddevicesmodel>
                      // System.out.println(gson.toJson(newsItems));
                    setUserLiveData((item as ArrayList<Adddevicesmodel>?)!!)
                }
                setIsLoading(false)
            }
        }

        override fun onFailure(call: Call<WebResponse<Any?>?>, t: Throwable) {
            t.cause
            setIsLoading(false)
        }
    }


    fun getDataList(tag: String?): List<Adddevicesmodel?>? {

        var datalist: List<Adddevicesmodel> = ArrayList<Adddevicesmodel>()
        val gson = Gson()
        datalist = gson.fromJson<List<Adddevicesmodel>>(tag, object : TypeToken<List<Adddevicesmodel?>?>() {}.type)
        return datalist
    }

    fun getlocation(tag: String?): LastModel {

        var datalist: LastModel = LastModel()
        val gson = Gson()
        datalist = gson.fromJson<LastModel>(tag, object : TypeToken<LastModel?>() {}.type)
        return datalist
    }

    inner class AddCallback : Callback<WebResponse<Any?>?> {
        override fun onResponse(call: Call<WebResponse<Any?>?>, response: Response<WebResponse<Any?>?>) {
            if (webservices != null) {
                var api_responces: Boolean = UIHelper.validateIfWebResponse(response, act)
                if (api_responces) {
                    val Response: WebResponse<Any?>? = response.body()
                    setReUserLiveData(Response)

                }
                setIsLoading(false)
            } else {
            }
        }

        override fun onFailure(call: Call<WebResponse<Any?>?>, t: Throwable) {
            t.cause
        }
    }

}