package com.appsnado.haippNew.retro


public object WebServiceConstants {
    const val READTIMEOUT = 20
    const val WRITETIMEOUT = 150

    const val PARAMS_TOKEN_EXPIRE = 401
    const val PARAMS_BADREQUEST = 400
    const val PARAMS_TOKEN_BLACKLIST = 402
    const val SUCCESSRESULT = 200

    //val BASE_URL: String? = "https://karvefitness.com/apis/api/"
    val BASE_URL2: String? = "http://10.0.4.35:3001/api/"


    //val BASE_URL: String? = "http://server.appsstaging.com:3001/api/"

    val BASE_URL: String? = "https://server.appsstaging.com/1461/haipp/public/api/"
    ///https://server.appsstaging.com/1461/haipp/public/api/

    var userid: String? = ""
    var devices_id: String? = ""
    val LOGIN: String? = "login"
    val SIGNUP: String? = "signup"
    val FORGET_PASSWORD: String? = "forgot_password"
    val CREATE_PROFILE: String? = "create_user_profile"
    val wall_feed: String? = "wall_feed"
    val LOGOUT: String? = "logout"
    val verification : String? = "verification_code"
    val RESEND: String? = "resend_code"
    val DEVICE_TYPE: String? = "android"
    val CHNAGE: String? = "change_password"
    var trainerprofile = false
    var prefToken: String? = null
    var usertype = 0
    var userfeedid: String? = null
    var postuserid: String? = ""
    var postusertype: String? = ""
    var typeselfid: String? = ""
    var statclayout: String? = "1"
}