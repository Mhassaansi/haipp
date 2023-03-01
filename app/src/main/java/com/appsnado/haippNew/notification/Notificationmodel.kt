package com.appsnado.haippNew.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Notificationmodel {

    @Expose
    @SerializedName("notification_id")
    internal var notification_id: String? = null


    @Expose
    @SerializedName("notification_title")
    internal var notification_title: String? = null


    @Expose
    @SerializedName("notification_message")
    internal var notification_message: String? = null


    @Expose
    @SerializedName("notification_type")
    internal var notification_type: String? = null



    @Expose
    @SerializedName("created_at")
    internal var created_at: String? = null



    @Expose
    @SerializedName("updated_at")
    internal var updated_at: String? = null


}