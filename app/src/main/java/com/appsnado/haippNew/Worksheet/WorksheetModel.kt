package com.appsnado.haippNew.Worksheet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WorksheetModel {

    @Expose
    @SerializedName("wt_id")
    internal var wt_id: String? = null


    @Expose
    @SerializedName("wt_user_id")
    internal var wt_user_id: String? = null


    @Expose
    @SerializedName("wt_device_id")
    internal var wt_device_id: String? = null



    @Expose
    @SerializedName("wt_description")
    internal var wt_description: String? = null


    @Expose
    @SerializedName("wt_start_date")
    internal var wt_start_date: String? = null




    @Expose
    @SerializedName("wt_subject_id")
    internal var wt_subject_id: String? = null


    @Expose
    @SerializedName("wt_end_date")
    internal var wt_end_date: String? = null
//    @Expose
//    @SerializedName("wt_json_object")
//    internal var wt_json_object: String? = null
    @Expose
    @SerializedName("wt_reward")
    internal var wt_reward: String? = null



    @Expose
    @SerializedName("wt_is_blocked")
    internal var wt_is_blocked: String? = null


    @Expose
    @SerializedName("created_at")
    internal var created_at: String? = null

    @Expose
    @SerializedName("updated_at")
    internal var updated_at: String? = null
}