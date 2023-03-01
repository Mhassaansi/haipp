package com.appsnado.haippNew.retro

import android.app.Activity
import android.util.Log
import com.appsnado.haippNew.data.SharedPreferenceManager
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WebServiceFactory(activity: Activity?) {
    // Activity activity;
    private val service_api: WebService?
    fun getservices(): WebService? {
        return service_api
    }


    companion object {
        private var instance: WebServiceFactory? = null
        private val dispatcher: Dispatcher? = Dispatcher()
        fun getInstance(act: Activity?): WebServiceFactory? {
            if (instance == null) {
                instance = WebServiceFactory(act)
            }
            return instance
        }
    }

    init {
        val prefHelper = SharedPreferenceManager(activity)
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpclient = OkHttpClient.Builder()
        httpclient.readTimeout(WebServiceConstants.READTIMEOUT.toLong(), TimeUnit.SECONDS)
        httpclient.connectTimeout(WebServiceConstants.READTIMEOUT.toLong(), TimeUnit.SECONDS)
        httpclient.writeTimeout(WebServiceConstants.WRITETIMEOUT.toLong(), TimeUnit.SECONDS)
        httpclient.addInterceptor(logging)
        httpclient.dispatcher(dispatcher!!)
        httpclient.addInterceptor { chain ->
            val original = chain.request()
            var authToken: String? = ""
            //String authValue = "";
            authToken = if (prefHelper.getToken() != null) {
                prefHelper.getToken()!!
            } else {
                ""
            }
            Log.d("token", "intercept: $authToken")
            val requestBuilder = original.newBuilder().addHeader("Authorization", "Bearer " +authToken)
                    .addHeader("Accept", "application/json")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
         val mRetrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(WebServiceConstants.BASE_URL).client(httpclient.build()).build()
          service_api = mRetrofit.create(WebService::class.java)
    }





}





