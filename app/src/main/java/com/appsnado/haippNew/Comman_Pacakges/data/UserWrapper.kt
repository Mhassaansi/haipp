package com.appsnado.haippNew.Comman_Pacakges.data

import android.text.TextUtils
import android.widget.EditText
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File
import java.io.Serializable

class UserWrapper : BaseObservable(), Serializable {
    private var isToggleOn = false
    private var isToggleon2 = false

    @Expose
    @SerializedName("user_id")
    internal var userID: String? = null



    @Expose
    @SerializedName("user_email")
     var userEmail: String? = null

    @Expose
    @SerializedName("user_password")
     var user_password: String? = null

    @Expose
    @SerializedName("user_social_token")
     var user_social_token: String? = null

    @Expose
    @SerializedName("user_device_type")
     var user_device_type: String? = null

    @Expose
    @SerializedName("user_social_type")
     var user_social_type: String? = null

    @Expose
    @SerializedName("user_device_token")
     var user_device_token: String? = null

    @Expose
    @SerializedName("user_is_forgot")
     var user_is_forgot: String? = null

    @Expose
    @SerializedName("user_is_verified")
     var user_is_verified: String? = null


    @Expose
    @SerializedName("created_at")
     var created_at: String? = null

    @Expose
    @SerializedName("updated_at")
     var updated_at: String? = null




    var imageFile: File? = null
    fun getPassword(): String? {
        return password
    }

    @Bindable
    fun setPassword(password: String?) {
        this.password = password
        // notifyPropertyChanged(BR.password);
    }

    internal var password: String? = null
    fun getUserID(): String? {
        return userID
    }

    fun setUserID(userID: String?) {
        this.userID = userID
    }








//
//    @Bindable
//    fun setUserWeight(userWeight: String?) {
//        this.userWeight = userWeight
//        // notifyPropertyChanged(BR.userWeight);
//    }
//
//    fun getUserObjectives(): String? {
//        return userObjectives
//    }
//
//    @Bindable
//    fun setUserObjectives(userObjectives: String?) {
//        this.userObjectives = userObjectives
//    }
//
//
//




    fun toggleVisibility(toggleOn: Boolean, toggleOn2: Boolean) {
        isToggleOn = toggleOn
        isToggleon2 = toggleOn2
        //notifyPropertyChanged(BR.toggleOn)
       // notifyPropertyChanged(BR.toggleOn2)
    }


    @Bindable
    fun getToggleOn(): Boolean {
        return isToggleOn
    }

    @Bindable
    fun getToggleOn2(): Boolean {
        return isToggleon2
    }

    companion object {
        @BindingAdapter("passwordValidator")
        fun passwordValidator(editText: EditText, password: String?) {
            // ignore infinite loops
            val minimumLength = 5
            if (TextUtils.isEmpty(password)) {
                editText.error = null
                return
            }
            if (editText.text.toString().length < minimumLength) {
                editText.error = "Password must be minimum $minimumLength length"
            } else editText.error = null
        }
    }
}