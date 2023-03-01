package com.appsnado.haippNew.Smartschedule

import androidx.databinding.BaseObservable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SmartschedulModel {


    @Expose
    @SerializedName("ss_id")
    internal var ss_id  : String? = null


    @Expose
    @SerializedName("ss_device_id")
    internal var ss_device_id: String? = null


    @Expose
    @SerializedName("ss_title")
    internal var ss_title: String? = null


    @Expose
    @SerializedName("ss_day")
    internal var ss_day: String? = null




    @Expose
    @SerializedName("ss_start_time")
    internal var ss_start_time: String? = null
    @Expose
    @SerializedName("ss_end_time")
    internal var ss_end_time : String? = null

    @Expose
    @SerializedName("ss_is_blocked")
    internal var ss_is_blocked: String? = null


    @Expose
    @SerializedName("created_at")
    internal var created_at: String? = null




    @Expose
    @SerializedName("updated_at")
    internal var updated_at: String? = null

}