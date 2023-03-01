package com.appsnado.haippNew.Worksheet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GradeModel {

        @Expose
        @SerializedName("grade_id")
        internal var grade_id: String? = null
        @Expose
        @SerializedName("grade_title")
        internal var grade_title: String? = null
        @Expose
        @SerializedName("grade_is_blocked")
        internal var grade_is_blocked: String? = null
        @Expose
        @SerializedName("created_at")
        internal var created_at: String? = null
        @Expose
        @SerializedName("updated_at")
        internal var updated_at: String? = null


    }