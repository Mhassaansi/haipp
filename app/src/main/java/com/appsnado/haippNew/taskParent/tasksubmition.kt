package com.appsnado.haippNew.taskParent

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class tasksubmition {

    @Expose
    @SerializedName("ts_id")
    internal var ts_id : String? = null


    @Expose
    @SerializedName("ts_task_id")
    internal var ts_task_id : String? = null


    @Expose
    @SerializedName("ts_device_id")
    internal var ts_device_id : String? = null


    @Expose
    @SerializedName("ts_date")
    internal var ts_date : String? = null

    @Expose
    @SerializedName("ts_image")
    internal var ts_image : String? = null

    @Expose
    @SerializedName("ts_reward")
    internal var ts_reward : String? = null

    @Expose
    @SerializedName("ts_status")
    internal var ts_status : String? = null


    @Expose
    @SerializedName("created_at")
    internal var created_at : String? = null

    @Expose
    @SerializedName("updated_at")
    internal var updated_at : String? = null



}