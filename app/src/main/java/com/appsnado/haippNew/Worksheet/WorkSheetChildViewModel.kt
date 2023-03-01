package com.appsnado.haippNew.Worksheet

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
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class WorkSheetChildViewModel(services: WebServiceFactory, activity: Activity?) : BaseViewModel<Any>() {
    private val webservices: WebServiceFactory
    var preferenceManag: SharedPreferenceManager? = null
    val userLiveData: MutableLiveData<ArrayList<Questionmodel>>
    val ReuserLiveData: MutableLiveData<WebResponse<Any?>?>
    val submitLiveData: MutableLiveData<WebResponse<Any?>?>
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
        submitLiveData= MutableLiveData()
        preferenceManag = SharedPreferenceManager(act)

    }

    fun loadDataNetwork(id: String, type: String) {
        setIsLoading(true)
        webservices!!.getservices()!!.getworksheetlist(id,type)!!.enqueue(ReResponcesCallback())
    }


    fun getuserLiveData(): MutableLiveData<ArrayList<Questionmodel>> {
        return userLiveData
    }

    private fun setUserLiveData(use: ArrayList<Questionmodel>) {
        setIsLoading(false)
        userLiveData.postValue(use)
    }

    private fun setReUserLiveData(use: WebResponse<Any?>?) {
        setIsLoading(false)
        ReuserLiveData.postValue(use)
    }


    private fun setsubmit(use: WebResponse<Any?>?) {
        setIsLoading(false)
        submitLiveData.postValue(use)
    }

    private fun setIsLoading(loading: Boolean) {
        loadingStatus.postValue(loading)
    }

    private fun createRequestBodyString(text: String?): RequestBody? {
        return if (text != null) {
            RequestBody.create(MultipartBody.FORM, text)
        } else null
    }

    fun addeworksheet(userID: String?, getdevicesid: String?, subjectId: String, name: String, point: String, stime: String, etime: String) {
        setIsLoading(true)
        webservices!!.getservices()!!.worksheet_task_create(userID,getdevicesid,subjectId,name,stime,etime,point)!!.enqueue(AddCallback())
    }
    fun asssubmit(getdevicesid: String?, subjectId: String?, obj: JSONObject) {
        setIsLoading(true)
        webservices!!.getservices()!!.submmittask(subjectId!!,getdevicesid!!,obj)!!.enqueue(SubmitCallback())
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
                    var data = getDataList(strJson)
                    setUserLiveData((data as ArrayList<Questionmodel>?)!!)


                }
                setIsLoading(false)
            }
        }

        override fun onFailure(call: Call<WebResponse<Any?>?>, t: Throwable) {
            t.cause
            setIsLoading(false)
        }
    }

    fun getDataList(tag: String?): List<Questionmodel?>? {

        var datalist: List<Questionmodel> = ArrayList<Questionmodel>()
        val gson = Gson()
        datalist = gson.fromJson<List<Questionmodel>>(tag, object : TypeToken<List<Questionmodel?>?>() {}.type)
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

    inner class SubmitCallback : Callback<WebResponse<Any?>?> {
        override fun onResponse(call: Call<WebResponse<Any?>?>, response: Response<WebResponse<Any?>?>) {
            if (webservices != null) {
                var api_responces: Boolean = UIHelper.validateIfWebResponse(response, act)
                if (api_responces) {
                    val Response: WebResponse<Any?>? = response.body()
                    setsubmit(Response)

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