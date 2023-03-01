package com.appsnado.haippNew.Role

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.appnado.haipp.baseviewmodel.BaseViewModel
import com.appsnado.haippNew.Comman_Pacakges.data.UserWrapper
import com.appsnado.haippNew.Comman_Pacakges.retro.LengthArrayTypeAdapterFactory
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.retro.WebServiceFactory
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoleViewModel(getservices: WebServiceFactory, activity: Activity?) : BaseViewModel<Any>() {
    private val webservices: WebServiceFactory
    val userLiveData: MutableLiveData<UserWrapper?>
    val loadingStatus: MutableLiveData<Boolean>
    private var user: UserWrapper
    private var act: Activity? = null

    init {
        user = UserWrapper()
        webservices = getservices;
        act = activity
        loadingStatus = MutableLiveData()
        userLiveData = MutableLiveData()
    }

    fun loadDataNetwork(userid: String?,token :String?) {
        setIsLoading(true)
        webservices!!.getservices()!!.complete_profile(userid, "android", token)!!.enqueue(ResponcesCallback())
    }


    fun getuserLiveData(): MutableLiveData<UserWrapper?> {
        return userLiveData
    }

    private fun setUserLiveData(use: UserWrapper?) {
        setIsLoading(false)
        userLiveData.postValue(use)
    }

    private fun setIsLoading(loading: Boolean) {
        loadingStatus.postValue(loading)
    }

    private fun createRequestBodyString(text: String?): RequestBody? {
        return if (text != null) {
            RequestBody.create(MultipartBody.FORM, text)
        } else null
    }

    fun resend() {
        setIsLoading(true)
        //webservices!!.getservices()!!.verification(userid, code, "android", "abcd")!!.enqueue(ResponcesCallback())
    }


    private inner class ResponcesCallback : Callback<WebResponse<Any?>?> {
        override fun onResponse(call: Call<WebResponse<Any?>?>, response: Response<WebResponse<Any?>?>) {
            if (webservices != null) {
                var api_responces: Boolean = UIHelper.validateIfWebResponse(response, act)
                if (api_responces) {
                    val gson = GsonBuilder()
                        .registerTypeAdapterFactory(LengthArrayTypeAdapterFactory.getLengthArrayTypeAdapterFactory())
                        .create()
                    val myJsonData = gson.toJson(response.body()?.data)
                    val elementsResponse: UserWrapper = gson.fromJson(myJsonData, UserWrapper::class.java)
                    System.out.println(gson.toJson(elementsResponse));
                    setUserLiveData(elementsResponse)
                }
                setIsLoading(false)
            }
        }

        override fun onFailure(call: Call<WebResponse<Any?>?>, t: Throwable) {
            t.cause
            setIsLoading(false)
        }
    }



}