package com.appsnado.haippNew.taskParent

import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class TaskdetailModel {
    @Expose
    @SerializedName("task_id")
    internal var task_id : String? = null


    @Expose
    @SerializedName("task_user_id")
    internal var task_user_id : String? = null


    @Expose
    @SerializedName("task_device_id")
    internal var task_device_id : String? = null


    @Expose
    @SerializedName("task_description")
    internal var task_description : String? = null


    @Expose
    @SerializedName("task_frequency")
    internal var task_frequency : String? = null

    @Expose
    @SerializedName("task_start_date")
    internal var task_start_date : String? = null

    @Expose
    @SerializedName("task_end_date")
    internal var task_end_date : String? = null



    @Expose
    @SerializedName("task_reward")
    internal var task_reward : String? = null

    @Expose
    @SerializedName("task_is_blocked")
    internal var task_is_blocked : String? = null



    @Expose
    @SerializedName("created_at")
    internal var created_at : String? = null



    @Expose
    @SerializedName("updated_at")
    internal var updated_at : String? = null


    @Expose
    @SerializedName("user_device_token")
    internal var user_device_token : String? = null




    @Expose
    @SerializedName("task_submition")
    val task_submition: ArrayList<tasksubmition>? = null





}