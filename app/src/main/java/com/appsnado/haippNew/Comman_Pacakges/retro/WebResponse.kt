package com.appsnado.haippNew.retro

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class WebResponse<T> : Serializable {

    @SerializedName("message")
    internal var message: String? = null


    @SerializedName("status")
    internal var status = 0


    @SerializedName("data")
    internal var data: T? = null




    @SerializedName("bearer_token")
    var token: String? = null
//
//
//
//    fun getToken(): String? {
//        return token
//    }
//
//    fun setToken(token: String?) {
//        this.token = token
//    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

//    fun getData(): T? {
//        return data
//    }
//
//    fun setData(data: T?) {
//        this.data = data
//    }

    fun getStatus(): Int {
        return status
    }

    fun setStatus(status: Int) {
        this.status = status
    }

    fun isSuccess(): Boolean {
        return if (status == 1) {
            true
        } else {
            false
        }
    }
}