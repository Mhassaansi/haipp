package com.appsnado.haippNew.Locationhistory

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.appnado.haipp.baseviewmodel.BaseViewModel
import com.appsnado.haippNew.Adddevices.Adddevicesmodel
import com.appsnado.haippNew.Comman_Pacakges.data.UserWrapper
import com.appsnado.haippNew.Helper.UIHelper
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
import java.util.ArrayList

class LocationhistroyViewModel(services: WebServiceFactory, activity: Activity?) : BaseViewModel<Any>() {
    private val webservices: WebServiceFactory
    var preferenceManag: SharedPreferenceManager? = null
    val userLiveData: MutableLiveData<ArrayList<locationhistroyModel>>
    val ReuserLiveData: MutableLiveData<WebResponse<Any?>?>
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
        preferenceManag = SharedPreferenceManager(act)

    }

    fun loadDataNetwork(id: String?, date: String) {
        setIsLoading(true)
        webservices!!.getservices()!!.getdevices(id, date)!!.enqueue(ReResponcesCallback())
    }


    fun getuserLiveData(): MutableLiveData<ArrayList<locationhistroyModel>> {
        return userLiveData
    }

    private fun setUserLiveData(use: ArrayList<locationhistroyModel>) {
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

    fun resend(devicesid: String, name: String?) {
        setIsLoading(true)
        webservices!!.getservices()!!.device_create(WebServiceConstants.userid, devicesid, name)!!
                .enqueue(AddCallback())
    }


    private inner class ReResponcesCallback : Callback<WebResponse<Any?>?> {
        override fun onResponse(
                call: Call<WebResponse<Any?>?>,
                response: Response<WebResponse<Any?>?>
        ) {
            if (webservices != null) {
                var api_responces: Boolean = UIHelper.validateIfWebResponse(response, act)
                if (api_responces) {
//                    val gson = GsonBuilder()
//                        .registerTypeAdapterFactory(LengthArrayTypeAdapterFactory.getLengthArrayTypeAdapterFactory())
//                        .create()
                    //     val myJsonData = gson.toJson(response.body()?.data)
                    var newsItems = ArrayList<Adddevicesmodel>()
                    //val elementsResponse: WebResponse<List<Adddevicesmodel?>?>? = gson.fromJson(myJsonData, Adddevicesmodel::class.java)
                    // newsItems = response.body()!!.data as ArrayList<locationhistroyModel>
                    //System.out.println(gson.toJson(newsItems));

                    val gson = Gson()
                    val strJson = gson.toJson(response.body()?.data)
                    var data = getDataList(strJson)
                    if(data != null)
                      setUserLiveData((data as ArrayList<locationhistroyModel>?)!!)

                    //setUserLiveData(newsItems!!)
                }
                setIsLoading(false)
            }
        }

        override fun onFailure(call: Call<WebResponse<Any?>?>, t: Throwable) {
            t.cause
            setIsLoading(false)
        }
    }


    inner class AddCallback : Callback<WebResponse<Any?>?> {
        override fun onResponse(
                call: Call<WebResponse<Any?>?>,
                response: Response<WebResponse<Any?>?>
        ) {
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

    fun getDataList(tag: String?): List<locationhistroyModel?>? {

        try{


        var datalist: List<locationhistroyModel> = ArrayList<locationhistroyModel>()
        val gson = Gson()
        datalist = gson.fromJson<List<locationhistroyModel>>(tag, object : TypeToken<List<locationhistroyModel?>?>() {}.type)
        return datalist
        }catch (e :Exception){
            e.stackTrace
        }
        return null
    }
}