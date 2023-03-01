package com.appsnado.haippNew.taskParent

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class claimModel {

        @Expose
    @SerializedName("id")
    internal var id: Int? = null


    @Expose
    @SerializedName("description")
    internal var description: String? = null


    @Expose
    @SerializedName("start_date")
    internal var start_date: String? = null
    @Expose
    @SerializedName("end_date")
    internal var end_date: String? = null
    @Expose
    @SerializedName("reward")
    internal var reward: String? = null

    @Expose
    @SerializedName("task_reward")
    internal var task_reward: String? = null


    @Expose
    @SerializedName("status")
    internal var status: String? = null

    @Expose
    @SerializedName("submition")
    internal var submition  : String? = null

    @Expose
    @SerializedName("created_at")
    internal var created_at: String? = null



    @Expose
    @SerializedName("frequency")
    internal var frequency: String? = null



    @Expose
    @SerializedName("submition_date")
    internal var submition_date: String? = null

    @Expose
    @SerializedName("type")
    internal var type: String? = null

//    @Expose
//    @SerializedName("ts_id")
//    internal var ts_id: Int? = null
//
//
//    @Expose
//    @SerializedName("task_description")
//    internal var task_description: String? = null
//
//    @Expose
//    @SerializedName("task_frequency")
//    internal var task_frequency: String? = null
//
//
//    @Expose
//    @SerializedName("task_start_date")
//    internal var task_start_date: String? = null
//    @Expose
//    @SerializedName("task_end_date")
//    internal var task_end_date: String? = null
//    @Expose
//    @SerializedName("task_reward")
//    internal var task_reward: String? = null
//    @Expose
//    @SerializedName("ts_date")
//    internal var ts_date: String? = null
//
//
//    @Expose
//    @SerializedName("ts_image")
//    internal var ts_image: String? = null
//    @Expose
//    @SerializedName("ts_status")
//    internal var ts_status: String? = null
//    @Expose
//    @SerializedName("created_at")
//    internal var created_at: String? = null
}