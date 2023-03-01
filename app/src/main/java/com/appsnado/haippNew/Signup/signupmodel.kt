package com.appsnado.haippNew.Signup

import androidx.databinding.BaseObservable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class signupmodel : BaseObservable(), Serializable {


    @Expose
    @SerializedName("user_id")
    internal var userID: Int? = null

}