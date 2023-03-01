package com.appsnado.haippNew

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.appnado.haipp.baseviewmodel.BaseViewModel
import com.appsnado.haippNew.Comman_Pacakges.data.UserWrapper
import com.appsnado.haippNew.Comman_Pacakges.retro.LengthArrayTypeAdapterFactory.getLengthArrayTypeAdapterFactory
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Signup.signupmodel
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.retro.WebServiceFactory
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupViewModel(service: WebServiceFactory, activity: Activity?) : BaseViewModel<Any>() {
    private val webservices: WebServiceFactory
    val userLiveData: MutableLiveData<signupmodel?>
    val loadingStatus: MutableLiveData<Boolean>
    private var user: UserWrapper
    private var act: Activity? = null

    init {

        user = UserWrapper()
        webservices = service;
        act = activity
        loadingStatus = MutableLiveData()
        userLiveData = MutableLiveData()

    }

    fun loadDataNetwork(userEmail: String?, password: String?) {
        setIsLoading(true)
        webservices!!.getservices()!!.signup(userEmail, password)!!.enqueue(ResponcesCallback())

    }


    fun getuserLiveData(): MutableLiveData<signupmodel?> {
        return userLiveData
    }

    private fun setUserLiveData(use: signupmodel) {
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


//    {
//        "status": 1,
//        "message": "User create successfully, visit your email verification code has been send",
//        "data": {
//        "user_id": 7
//    }
//    }


    private inner class ResponcesCallback : Callback<WebResponse<Any?>?> {
        override fun onResponse(call: Call<WebResponse<Any?>?>, response: Response<WebResponse<Any?>?>) {
            if(webservices != null){
                var api_responces : Boolean = UIHelper.validateIfWebResponse(response,act)
                   if(api_responces){
                       val gson = GsonBuilder()
                               .registerTypeAdapterFactory(getLengthArrayTypeAdapterFactory())
                               .create()
                       val myJsonData = gson.toJson(response.body()?.data)
                       val elementsResponse: signupmodel = gson.fromJson(myJsonData, signupmodel::class.java)
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

//            if (webservices != null && response.body() != null && Objects.requireNonNull(response.body())!!.getStatus() == 1) {
//                val userresponces: WebResponse<signupmodel?>? = response.body()
//                setUserLiveData(userresponces)
//                setIsLoading(false)
//             } else {
//                if(response.code() == 400){
//                  val userData: BufferedSource = response.errorBody()!!.source()
//                }
//                 //UIHelper.showcustomsnackbar(act,userRespons)}
//                setIsLoading(false)
//            }