package com.appsnado.haippNew.Worksheet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SubjectModel {

    @Expose
    @SerializedName("subject_id")
    internal var subject_id: String? = null
    @Expose
    @SerializedName("subject_grade_id")
    internal var subject_grade_id: String? = null
    @Expose
    @SerializedName("subject_title")
    internal var subject_title: String? = null
    @Expose
    @SerializedName("subject_is_blocked")
    internal var subject_is_blocked: String? = null
    @Expose
    @SerializedName("created_at")
    internal var created_at: String? = null


}