package com.appsnado.haippNew

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.appnado.haipp.baseviewmodel.BaseViewModel
import com.appsnado.haippNew.Comman_Pacakges.data.UserWrapper
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.retro.WebServiceFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetViewModel(services: WebServiceFactory, activity: Activity?) : BaseViewModel<Any>() {
    private val webservices: WebServiceFactory
     public val userLiveData: MutableLiveData<WebResponse<Any?>?>
    val loadingStatus: MutableLiveData<Boolean>
    private  var user: UserWrapper
    private var act: Activity? = null

    init {
        user = UserWrapper()
        webservices =   services
        act = activity
        loadingStatus = MutableLiveData()
        userLiveData =  MutableLiveData()
    }

    fun loadDataNetwork(userEmail: String?) {
        setIsLoading(true)
        webservices!!.getservices()!!.forgetpass(userEmail)!!.enqueue(ResponcesCallback())
    }


    fun getuserLiveData(): MutableLiveData<WebResponse<Any?>?> {
        return userLiveData
    }

    private fun setUserLiveData(use: WebResponse<Any?>?) {
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

    private inner class ResponcesCallback : Callback<WebResponse<Any?>?> {
        override fun onResponse(call: Call<WebResponse<Any?>?>, response: Response<WebResponse<Any?>?>) {
            if(webservices != null){
                var api_responces : Boolean = UIHelper.validateIfWebResponse(response,act)
                if(api_responces){
                    val userresponces: WebResponse<Any?>? = response.body()
//                    val gson = GsonBuilder()
//                            .registerTypeAdapterFactory(LengthArrayTypeAdapterFactory.getLengthArrayTypeAdapterFactory())
//                            .create()
                   // val myJsonData = gson.toJson(response.body()?.data)
                   // val elementsResponse: WebResponse = gson.fromJson(myJsonData, WebResponse::class.java)
                    //System.out.println(gson.toJson(elementsResponse));
                    setUserLiveData(userresponces)
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