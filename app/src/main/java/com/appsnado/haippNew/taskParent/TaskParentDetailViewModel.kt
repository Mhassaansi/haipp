package com.appsnado.haippNew.taskParent

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.appnado.haipp.baseviewmodel.BaseViewModel
import com.appsnado.haippNew.Comman_Pacakges.data.UserWrapper
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.data.SharedPreferenceManager
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.retro.WebServiceConstants
import com.appsnado.haippNew.retro.WebServiceFactory
import com.appsnado.haippNew.taskParent.TaskDetailFragment.Companion.jsondatearray
import com.appsnado.haippNew.taskParent.TaskDetailFragment.Companion.jsonkeydata
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TaskParentDetailViewModel(services: WebServiceFactory, activity: Activity?) : BaseViewModel<Any>() {
    private val webservices: WebServiceFactory
    var preferenceManag: SharedPreferenceManager? = null
    val userLiveData: MutableLiveData<TaskdetailModel>
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

    fun loadDataNetwork(code: String) {
        setIsLoading(true)
        webservices!!.getservices()!!.gettaskdetail(code)!!.enqueue(ReResponcesCallback())
    }


    fun getuserLiveData(): MutableLiveData<TaskdetailModel>{
        return userLiveData
    }

    private fun setUserLiveData(use: TaskdetailModel) {
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

    fun addtask(getdevicesid: String?, des: String, freq: String, sdate: String, edate: String, point: String,json : JSONObject) {
        setIsLoading(true)
        webservices!!.getservices()!!.task_create(WebServiceConstants.userid,getdevicesid,des,freq,sdate,edate,point,json)!!.enqueue(AddCallback())
    }



    fun submittask(getdevicesid: String?, taskid: String, date: String, image: MultipartBody.Part?) {
        setIsLoading(true)
        webservices!!.getservices()!!.task_submit(createRequestBodyString(getdevicesid),createRequestBodyString(taskid),createRequestBodyString(date),image)!!.enqueue(AddCallback())
    }


    fun statuschange(id: String?, status: String) {
        setIsLoading(true)
        webservices!!.getservices()!!.changestatus(status,id)!!.enqueue(AddCallback())
    }


    private inner class ReResponcesCallback : Callback<WebResponse<Any?>?> {
        override fun onResponse(call: Call<WebResponse<Any?>?>, response: Response<WebResponse<Any?>?>) {
            if (webservices != null) {
                var api_responces: Boolean = UIHelper.validateIfWebResponse(response, act)
                if (api_responces) {

                    val gson = Gson()
                    val strJson = gson.toJson(response.body()?.data )
                    var jsonObj =  JSONObject(strJson);
                   /// val sync_reponse: JSONObject = jsonObj.getJSONObject("data")
                    jsonkeydata = jsonObj.getString("task_frequency")
                    val sync_reponse: JSONObject = jsonObj.getJSONObject("task_json_object")
                    if(jsonkeydata == "Weekly"){
                        jsondatearray = sync_reponse.getJSONArray("Weekly")
                    }else if(jsonkeydata == "Monthly"){
                        jsondatearray = sync_reponse.getJSONArray("Monthly")
                    }



                    var data = getDataList(strJson)
                    setUserLiveData((data as TaskdetailModel?)!!)


                }
                setIsLoading(false)
            }
        }

        override fun onFailure(call: Call<WebResponse<Any?>?>, t: Throwable) {
            t.cause
            setIsLoading(false)
        }
    }

    fun getDataList(tag: String?): TaskdetailModel? {

        var datalist: TaskdetailModel= TaskdetailModel()
        val gson = Gson()
        datalist = gson.fromJson<TaskdetailModel>(tag, object : TypeToken<TaskdetailModel?>() {}.type)
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