package com.appsnado.haippNew.GeoFences

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GeoFencesModel {

    @Expose
    @SerializedName("dc_id")
    internal var dc_id: String? = null

    @Expose
    @SerializedName("dc_latitude")
    internal var dc_latitude: String? = null

    @Expose
    @SerializedName("dc_longitude")
    internal var dc_longitude: String? = null

    @Expose
    @SerializedName("dc_radius")
    internal var dc_radius: String? = null

    @Expose
    @SerializedName("updated_at")
    internal var updated_at: String? = null

    ///
    @Expose
    @SerializedName("dc_device_id")
    internal var dc_device_id: String? = null

    @Expose
    @SerializedName("dc_address")
    internal var dc_address: String? = null


    @Expose
    @SerializedName("created_at")
    internal var created_at: String? = null


    @Expose
    @SerializedName("dc_title")
    internal var dc_title: String? = null


}