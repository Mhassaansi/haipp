package com.appsnado.haippNew.Chat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigInteger
import java.util.*

class Messangermodel {
//    fun getcreatedAt(): Object? {
//        return createdAt
//    }


    fun getcreatedAt(): Object? {
        if (createdAt == null) {
            return null
        }else {
            isNonIntegerDouble(createdAt.toString())
            return createdAt
        }

    }

    fun isNonIntegerDouble(`in`: String): Boolean {
        try {
            `in`.toDouble()
        } catch (nfe: NumberFormatException) {
            return false
        }
        try {
            BigInteger(`in`)
        } catch (nfe: NumberFormatException) {
            return true
        }
        return false
    }


    fun setcreatedAt(sendercounter: Long) {
        createdAt = sendercounter as Object
    }

    fun getLoginUserId(): String? {
        return loginUserId
    }

    fun setLoginUserId(loginUserId: String?) {
        this.loginUserId = loginUserId
    }

    fun getLoginUserImage(): String? {
        return loginUserImage
    }

    fun setLoginUserImage(loginUserImage: String?) {
        this.loginUserImage = loginUserImage
    }

    fun getLoginUserName(): String? {
        return loginUserName
    }

    fun setLoginUserName(loginUserName: String?) {
        this.loginUserName = loginUserName
    }

    fun getLoginUserType(): String? {
        return loginUserType
    }

    fun setLoginUserType(loginUserType: String?) {
        this.loginUserType = loginUserType
    }

    fun getOtherUserId(): String? {
        return otherUserId
    }

    fun setOtherUserId(otherUserId: String?) {
        this.otherUserId = otherUserId
    }

    fun getOtherUserImage(): String? {
        return otherUserImage
    }

    fun setOtherUserImage(otherUserImage: String?) {
        this.otherUserImage = otherUserImage
    }

    fun getOtherUserName(): String? {
        return otherUserName
    }

    fun setOtherUserName(otherUserName: String?) {
        this.otherUserName = otherUserName
    }

    fun getOtherUserType(): String? {
        return otherUserType
    }

    fun setOtherUserType(otherUserType: String?) {
        this.otherUserType = otherUserType
    }

    @SerializedName("createdAt")
    @Expose
    private var createdAt: Object? = null

    @SerializedName("loginUserId")
    @Expose
    private var loginUserId: String? = null

    @SerializedName("loginUserImage")
    @Expose
    private var loginUserImage: String? = null

    @SerializedName("loginUserName")
    @Expose
    private var loginUserName: String? = null

    @SerializedName("loginUserType")
    @Expose
    private var loginUserType: String? = null

    @SerializedName("otherUserId")
    @Expose
    private var otherUserId: String? = null

    @SerializedName("otherUserImage")
    @Expose
    private var otherUserImage: String? = null

    @SerializedName("otherUserName")
    @Expose
    private var otherUserName: String? = null

    @SerializedName("otherUserType")
    @Expose
    private var otherUserType: String? = null



    @SerializedName("user_device_token")
    @Expose
    private var user_device_token: String? = null

    fun getdeviceid(): String? {
        return user_device_token
    }

    fun setdeviceid(count: String?) {
        this.user_device_token = count
    }


    @SerializedName("isSeen")
    @Expose
    private  var isSeen: Boolean? = null


    private  var countmsm: Int? = 0


    fun getcount(): Int? {
        return countmsm
    }

    fun setcount(count: Int?) {
        this.countmsm = count
    }


    fun getisSeen(): Boolean? {
        return isSeen
    }

    fun setisSeen(isseen: Boolean?) {
        isSeen = isseen
    }

}