package com.appsnado.haippNew

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.appnado.haipp.baseviewmodel.BaseViewModel
import com.appsnado.haippNew.Comman_Pacakges.data.UserWrapper
import com.appsnado.haippNew.Comman_Pacakges.retro.LengthArrayTypeAdapterFactory
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.data.SharedPreferenceManager
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.retro.WebServiceConstants
import com.appsnado.haippNew.retro.WebServiceConstants.userid
import com.appsnado.haippNew.retro.WebServiceFactory
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerificationViewModel(getservices: WebServiceFactory, activity: Activity?) : BaseViewModel<Any>() {
    private val webservices: WebServiceFactory
    var preferenceManag: SharedPreferenceManager? = null
    val userLiveData: MutableLiveData<UserWrapper?>
    val ReuserLiveData: MutableLiveData<WebResponse<Any?>?>
    val loadingStatus: MutableLiveData<Boolean>
    private var user: UserWrapper
    private var act: Activity? = null

    init {
        user = UserWrapper()
        webservices = getservices;
        act = activity
        loadingStatus = MutableLiveData()
        userLiveData = MutableLiveData()
        ReuserLiveData = MutableLiveData()
        preferenceManag = SharedPreferenceManager(act)

    }

    fun loadDataNetwork(code: String?, userid: String?) {
        setIsLoading(true)
        webservices!!.getservices()!!.verification(userid, code, "android", "abcd")!!.enqueue(ResponcesCallback())
    }


    fun getuserLiveData(): MutableLiveData<UserWrapper?> {
        return userLiveData
    }

    private fun setUserLiveData(use: UserWrapper?) {
        setIsLoading(false)
        userLiveData.postValue(use)
    }

    private fun setReUserLiveData(use: WebResponse<Any?>??) {
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

    fun resend() {
        setIsLoading(true)
         webservices!!.getservices()!!.reverification(userid)!!.enqueue(ReResponcesCallback())
    }




    private inner class ReResponcesCallback : Callback<WebResponse<Any?>?> {
        override fun onResponse(call: Call<WebResponse<Any?>?>, response: Response<WebResponse<Any?>?>) {
            if (webservices != null) {
                var api_responces: Boolean = UIHelper.validateIfWebResponse(response, act)
                if (api_responces) {
                    val userresponces: WebResponse<Any?>? = response.body()
                       setReUserLiveData(userresponces!!)
                 }
                setIsLoading(false)
            }
        }

        override fun onFailure(call: Call<WebResponse<Any?>?>, t: Throwable) {
            t.cause
            setIsLoading(false)
        }
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

                    preferenceManag!!.setIsLogin(true)
                    preferenceManag!!.putUser(elementsResponse)
                    WebServiceConstants.prefToken = response.body()!!.token
                    preferenceManag!!.setToken(response.body()!!.token)
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