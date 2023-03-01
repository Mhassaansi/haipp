package com.appsnado.haippNew.Locationhistory

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class locationhistroyModel {

    @Expose
    @SerializedName("cc_id")
    internal var cc_id: Double? = null

    @Expose
    @SerializedName("cc_device_id")
    internal var cc_device_id: String? = null


    @Expose
    @SerializedName("cc_title")
    internal var cc_title: String? = null


    @Expose
    @SerializedName("cc_latitude")
    internal var cc_latitude: Double? = null

    @Expose
    @SerializedName("cc_longitude")
    internal var cc_longitude: Double? = null


    @Expose
    @SerializedName("created_at")
    internal var created_at: String? = null



    @Expose
    @SerializedName("updated_at")
    internal var updated_at: String? = null


}