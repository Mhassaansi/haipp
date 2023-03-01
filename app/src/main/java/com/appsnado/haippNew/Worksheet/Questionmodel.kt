package com.appsnado.haippNew.Worksheet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Questionmodel {

    @Expose
    @SerializedName("question_id")
    internal var question_id: String? = null

    @Expose
    @SerializedName("question_question")
    internal var question_question: String? = null


    @Expose
    @SerializedName("question_option_1")
    internal var question_option_1: String? = null


    @Expose
    @SerializedName("question_option_2")
    internal var question_option_2: String? = null

    @Expose
    @SerializedName("question_option_3")
    internal var question_option_3: String? = null



    @Expose
    @SerializedName("question_correct_answer")
    internal var question_correct_answer: String? = null

}