package com.appsnado.haippNew.Smartschedule

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.appnado.haipp.baseviewmodel.BaseViewModel
import com.appsnado.haippNew.Comman_Pacakges.data.UserWrapper
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Worksheet.GradeModel
import com.appsnado.haippNew.Worksheet.SubjectModel
import com.appsnado.haippNew.Worksheet.WorksheetModel
import com.appsnado.haippNew.data.SharedPreferenceManager
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.retro.WebServiceConstants
import com.appsnado.haippNew.retro.WebServiceFactory
import com.appsnado.haippNew.taskParent.AddtaskModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class WorksheetViewModel(services: WebServiceFactory, activity: Activity?) : BaseViewModel<Any>() {
    private val webservices: WebServiceFactory
    var preferenceManag: SharedPreferenceManager? = null
    val userLiveData: MutableLiveData<ArrayList<WorksheetModel>>
    val ReuserLiveData: MutableLiveData<WebResponse<Any?>?>
    val loadingStatus: MutableLiveData<Boolean>
    private var user: UserWrapper
    val userGrade: MutableLiveData<ArrayList<GradeModel>>
    val userSubject: MutableLiveData<ArrayList<SubjectModel>>
    private var act: Activity? = null

    init {
        user = UserWrapper()
        webservices = services;
        act = activity
        loadingStatus = MutableLiveData()
        userLiveData = MutableLiveData()
        ReuserLiveData = MutableLiveData()
        userGrade = MutableLiveData()
        userSubject= MutableLiveData()
        preferenceManag = SharedPreferenceManager(act)

    }

    fun loadDataNetwork(getdevicesid: String?) {
        setIsLoading(true)
        webservices!!.getservices()!!.worksheetlsit(getdevicesid)!!.enqueue(ReResponcesCallback())
    }



    fun loadGrades() {
        setIsLoading(true)
        webservices!!.getservices()!!.worksheetgrade()!!.enqueue(GradeCallback())
    }



    fun loadSubject(s :String) {
        setIsLoading(true)
        webservices!!.getservices()!!.getsubject(s)!!.enqueue(SubjectCallback())
    }


    fun getuserLiveData(): MutableLiveData<ArrayList<WorksheetModel>> {
        return userLiveData
    }

    private fun setUserLiveData(use: ArrayList<WorksheetModel>) {
        setIsLoading(false)
        userLiveData.postValue(use)
    }

    private fun setUserGrade(use: ArrayList<GradeModel>) {
        setIsLoading(false)
        userGrade.postValue(use)
    }

    private fun setSubject(use: ArrayList<SubjectModel>) {
        setIsLoading(false)
        userSubject.postValue(use)
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

    private inner class GradeCallback : Callback<WebResponse<Any?>?> {
        override fun onResponse(call: Call<WebResponse<Any?>?>, response: Response<WebResponse<Any?>?>) {
            if (webservices != null) {
                var api_responces: Boolean = UIHelper.validateIfWebResponse(response, act)
                if (api_responces) {
                    val gson = Gson()
                    val strJson = gson.toJson(response.body()?.data )
                    var data = getDataListgrade(strJson)
                    setUserGrade((data as ArrayList<GradeModel>?)!!)


                }
                setIsLoading(false)
            }
        }

        override fun onFailure(call: Call<WebResponse<Any?>?>, t: Throwable) {
            t.cause
            setIsLoading(false)
        }
    }



    private inner class SubjectCallback : Callback<WebResponse<Any?>?> {
        override fun onResponse(call: Call<WebResponse<Any?>?>, response: Response<WebResponse<Any?>?>) {
            if (webservices != null) {
                var api_responces: Boolean = UIHelper.validateIfWebResponse(response, act)
                if (api_responces) {
                    val gson = Gson()
                    val strJson = gson.toJson(response.body()?.data )
                    var data = getsublist(strJson)
                    setSubject((data as ArrayList<SubjectModel>?)!!)


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
                    val gson = Gson()
                    val strJson = gson.toJson(response.body()?.data )
                    var data = getDataList(strJson)
                    setUserLiveData((data as ArrayList<WorksheetModel>?)!!)


                }
                setIsLoading(false)
            }
        }

        override fun onFailure(call: Call<WebResponse<Any?>?>, t: Throwable) {
            t.cause
            setIsLoading(false)
        }
    }

    fun getDataList(tag: String?): List<WorksheetModel?>? {

        var datalist: List<WorksheetModel> = ArrayList<WorksheetModel>()
        val gson = Gson()
        datalist = gson.fromJson<List<WorksheetModel>>(tag, object : TypeToken<List<WorksheetModel?>?>() {}.type)
        return datalist
    }

    fun getDataListgrade(tag: String?): List<GradeModel?>? {

        var datalist: List<GradeModel> = ArrayList<GradeModel>()
        val gson = Gson()
        datalist = gson.fromJson<List<GradeModel>>(tag, object : TypeToken<List<GradeModel?>?>() {}.type)
        return datalist
    }

    fun getsublist(tag: String?): List<SubjectModel?>? {

        var datalist: List<SubjectModel> = ArrayList<SubjectModel>()
        val gson = Gson()
        datalist = gson.fromJson<List<SubjectModel>>(tag, object : TypeToken<List<SubjectModel?>?>() {}.type)
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