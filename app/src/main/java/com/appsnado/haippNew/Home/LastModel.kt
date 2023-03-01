package com.appsnado.haippNew.Home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LastModel  {

    @Expose
    @SerializedName("cc_id")
    internal var cc_id: String? = null


    @Expose
    @SerializedName("cc_device_id")
    internal var cc_device_id: String? = null


    @Expose
    @SerializedName("cc_latitude")
    internal var cc_latitude: String? = null



    @Expose
    @SerializedName("cc_longitude")
    internal var cc_longitude: String? = null


    @Expose
    @SerializedName("created_at")
    internal var created_at: String? = null


    @Expose
    @SerializedName("updated_at")
    internal var updated_at: String? = null



}