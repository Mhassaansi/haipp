package com.appsnado.haippNew.Smartschedule

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.appnado.haipp.baseviewmodel.BaseViewModel
import com.appsnado.haippNew.Comman_Pacakges.data.UserWrapper
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.data.SharedPreferenceManager
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.retro.WebServiceFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SmartscheduleViewModel(services: WebServiceFactory, activity: Activity?) : BaseViewModel<Any>() {
    private val webservices: WebServiceFactory
    var preferenceManag: SharedPreferenceManager? = null
    val userLiveData: MutableLiveData<ArrayList<SmartschedulModel>>
    val ReuserLiveData: MutableLiveData<WebResponse<Any?>?>
    val DeleteLiveData: MutableLiveData<WebResponse<Any?>?>
    val loadingStatus: MutableLiveData<Boolean>
    private var user: UserWrapper
    var notify_s :String = ""
    private var act: Activity? = null

    init {
        user = UserWrapper()
        webservices = services;
        act = activity
        loadingStatus = MutableLiveData()
        userLiveData = MutableLiveData()
        ReuserLiveData = MutableLiveData()
        DeleteLiveData = MutableLiveData()
        preferenceManag = SharedPreferenceManager(act)

    }

    fun loadDataNetwork(code: String, s: String) {
        setIsLoading(true)
        notify_s = s
        webservices!!.getservices()!!.getsmartschedule(code)!!.enqueue(ReResponcesCallback())
    }


    fun getuserLiveData(): MutableLiveData<ArrayList<SmartschedulModel>> {
        return userLiveData
    }

    private fun setUserLiveData(use: ArrayList<SmartschedulModel>) {
        setIsLoading(false)
        userLiveData.postValue(use)
    }

    private fun setReUserLiveData(use: WebResponse<Any?>?) {
        setIsLoading(false)
        ReuserLiveData.postValue(use)
    }

    private fun setDeleteLiveData(use: WebResponse<Any?>?) {
        setIsLoading(false)
        DeleteLiveData.postValue(use)
    }

    private fun setIsLoading(loading: Boolean) {
        loadingStatus.postValue(loading)
    }

    private fun createRequestBodyString(text: String?): RequestBody? {
        return if (text != null) {
            RequestBody.create(MultipartBody.FORM, text)
        } else null
    }

    fun addtask(getdevicesid: String?, des: String, day: String, sdate: String, edate: String) {
        setIsLoading(true)
        webservices!!.getservices()!!.smartsch_create(getdevicesid,des,day,sdate,edate)!!.enqueue(AddCallback())
    }

    fun delet(id: String?) {
        setIsLoading(true)
        webservices!!.getservices()!!.deletdata(id)!!.enqueue(DeleteCallback())
    }



    private inner class ReResponcesCallback : Callback<WebResponse<Any?>?> {
        override fun onResponse(call: Call<WebResponse<Any?>?>, response: Response<WebResponse<Any?>?>) {
            if (webservices != null) {
                var api_responces: Boolean = UIHelper.validateIfWebResponse(response, act)
                if (api_responces) {
//                    val gson = GsonBuilder()
//                            .registerTypeAdapterFactory(LengthArrayTypeAdapterFactory.getLengthArrayTypeAdapterFactory())
//                            .create()
//                    val myJsonData = gson.toJson(response.body()?.data)
//                    var newsItems = ArrayList<AddtaskModel>()
//                    //val elementsResponse: WebResponse<List<Adddevicesmodel?>?>? = gson.fromJson(myJsonData, Adddevicesmodel::class.java)
//                    newsItems = response.body()!!.data as ArrayList<AddtaskModel>
//                    System.out.println(gson.toJson(newsItems));
//                    setUserLiveData(newsItems!!)

                    val gson = Gson()
                    val strJson = gson.toJson(response.body()?.data )
                    if(SmartscheduleFragment.not_detail != null){
                        SmartscheduleFragment.not_detail!!.firebasesetsmartsch(strJson)
                    }
                    if(notify_s == "1"){
                         notify_s = "0"

                        if(SmartscheduleFragment.not_detail != null){
                            SmartscheduleFragment.not_detail!!. NotificationSend("true","smartschedule",strJson)
                        }
                     }
                     var data = getDataList(strJson)
                     setUserLiveData((data as ArrayList<SmartschedulModel>?)!!)


                }else{
                    if(SmartscheduleFragment.not_detail != null){
                        SmartscheduleFragment.not_detail!!. NotificationSend("true","smartschedule","")
                    }
                }

                if(notify_s == "1"){
                    notify_s = "0"
                    if(SmartscheduleFragment.not_detail != null){
                        SmartscheduleFragment.not_detail!!. NotificationSend("true","smartschedule","")
                    }
                }
                setIsLoading(false)
            }
        }

        override fun onFailure(call: Call<WebResponse<Any?>?>, t: Throwable) {
            t.cause
            setIsLoading(false)
        }
    }

    fun getDataList(tag: String?): List<SmartschedulModel?>? {

        var datalist: List<SmartschedulModel> = ArrayList<SmartschedulModel>()
        val gson = Gson()
        datalist = gson.fromJson<List<SmartschedulModel>>(tag, object : TypeToken<List<SmartschedulModel?>?>() {}.type)
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


    inner class DeleteCallback : Callback<WebResponse<Any?>?> {
        override fun onResponse(call: Call<WebResponse<Any?>?>, response: Response<WebResponse<Any?>?>) {
            if (webservices != null) {
                var api_responces: Boolean = UIHelper.validateIfWebResponse(response, act)
                if (api_responces) {
                    val Response: WebResponse<Any?>? = response.body()
                    setDeleteLiveData(Response)

                }else{

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