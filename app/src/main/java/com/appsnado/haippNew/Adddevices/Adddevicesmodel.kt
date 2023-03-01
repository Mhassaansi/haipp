package com.appsnado.haippNew.Adddevices

import androidx.databinding.BaseObservable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Adddevicesmodel  : BaseObservable(), Serializable {

    @Expose
    @SerializedName("device_id")
    internal var devicesid: String? = null

    @Expose
    @SerializedName("device_user_id")
    internal var device_user_id: String? = null

    @Expose
    @SerializedName("device_title")
    internal var device_title: String? = null

    @Expose
    @SerializedName("device_type")
    internal var device_type: String? = null

    @Expose
    @SerializedName("device_token")
    internal var device_token: String? = null

     ///
    @Expose
    @SerializedName("device_verfied_token")
    internal var device_verfied_token: String? = null

    @Expose
    @SerializedName("created_at")
    internal var created_at: String? = null


    @Expose
    @SerializedName("updated_at")
    internal var updated_at: String? = null

    @Transient
    var isSelected = false
}