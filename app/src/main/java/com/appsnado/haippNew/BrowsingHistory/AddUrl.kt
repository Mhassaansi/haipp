package com.appsnado.haippNew.BrowsingHistory

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddUrl {

    @kotlin.jvm.JvmField
    @SerializedName("title")
    @Expose
    var title : String? = ""



    @kotlin.jvm.JvmField
    @SerializedName("url")
    @Expose
    var url: String? = ""


    @kotlin.jvm.JvmField
    @SerializedName("starttime")
    @Expose
    var starttime : String? = ""




    @kotlin.jvm.JvmField
    @SerializedName("endtime")
    @Expose
    var endtime : String? = ""
}