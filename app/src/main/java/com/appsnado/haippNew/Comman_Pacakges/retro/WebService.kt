package com.appsnado.haippNew.retro


import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface WebService {


    @FormUrlEncoded
    @POST("user_create")
    open fun signup(
        @Field("user_email") email: String?,
        @Field("user_password") pass: String?
    ): Call<WebResponse<Any?>?>?


    @FormUrlEncoded
    @POST("user_verification")
    open fun verification(
        @Field("user_id") user_id: String?,
        @Field("user_verified_code") user_verified_code: String?,
        @Field("user_device_type") user_device_type: String?,
        @Field("user_device_token") user_device_token: String?
    ): Call<WebResponse<Any?>?>?


//    @FormUrlEncoded
//    @POST("Get_Devices")
//    open fun getdevices(@Field("user_id") user_id: String?,
//                          @Field("user_verified_code") user_verified_code: String?,
//                          @Field("user_device_type") user_device_type: String?,
//                          @Field("user_device_token") user_device_token: String?): Call<WebResponse<Any?>?>?

    @GET("task/task_point")
    open fun claimlist(@Query("device_id") user_id: String?): Call<WebResponse<Any?>?>?


    @GET("task/task_list")
    open fun gettasklist(@Query("device_id") user_id: String?): Call<WebResponse<Any?>?>?


    @GET("worksheet/task_worksheet/child_task_list")
    open fun gettasklistchild(@Query("device_id") user_id: String?): Call<WebResponse<Any?>?>?


    @GET("worksheet/option/worksheet_question_list")
    open fun getworksheetlist(
        @Query("subject_id") subject_id: String?,
        @Query("type") type: String?
    ): Call<WebResponse<Any?>?>?


    @GET("worksheet/task_worksheet/child_point_list")
    open fun getclaimlist(@Query("device_id") user_id: String?): Call<WebResponse<Any?>?>?


    @GET("worksheet/option/grade_list")
    open fun worksheetgrade(): Call<WebResponse<Any?>?>?


    @GET("worksheet/worksheet_task_list")
    open fun worksheetlsit(@Query("device_id") user_id: String?): Call<WebResponse<Any?>?>?


    @GET("worksheet/option/subject_list")
    open fun getsubject(@Query("grade_id") user_id: String?): Call<WebResponse<Any?>?>?


    @GET("smart_schdelue/schdelue_list")
    open fun getsmartschedule(@Query("ss_device_id") user_id: String?): Call<WebResponse<Any?>?>?

    @GET("task/task_detail")
    open fun gettaskdetail(@Query("task_id") user_id: String?): Call<WebResponse<Any?>?>?


    @GET("device_list")
    open fun getdevices(@Query("user_id") user_id: String?): Call<WebResponse<Any?>?>?


    @GET("device/child_history")
    open fun getdevices(
        @Query("device_id") user_id: String?,
        @Query("to_date") to_date: String?
    ): Call<WebResponse<Any?>?>?


    @GET("device_coordinate/coordinate_list")
    open fun Alllocationlist(@Query("device_id") user_id: String?): Call<WebResponse<Any?>?>?

    @GET("re_send_code")
    open fun reverification(@Query("user_id") user_id: String?): Call<WebResponse<Any?>?>?

    @DELETE("device_delete")
    open fun devicedelete(@Query("device_id") user_id: String?): Call<WebResponse<Any?>?>?

    @GET("device/last_location_child")
    open fun getlocation(@Query("device_id") user_id: String?): Call<WebResponse<Any?>?>?



    @GET("list_notification")
    open fun getnotification(@Query("user_id") user_id: String?,@Query("type") type: String?): Call<WebResponse<Any?>?>?

    @FormUrlEncoded
    @POST("user_login")
    open fun login(
        @Field("user_email") user_email: String?,
        @Field("user_password") user_password: String?,
        @Field("user_device_type") user_device_type: String?,
        @Field("user_device_token") user_device_token: String?
    ): Call<WebResponse<Any?>?>?


    @FormUrlEncoded
    @POST("user_social_login")
    open fun Social(
        @Field("user_social_token") user_email: String?,
        @Field("user_social_type") user_password: String?
    ): Call<WebResponse<Any?>?>?


    @PUT("complete_profile")
    open fun complete_profile(
        @Query("user_id") user_id: String?,
        @Query("user_device_type") user_device_type: String?,
        @Query("user_device_token") user_device_token: String?
    ): Call<WebResponse<Any?>?>?


    @GET("logout")
    open fun logout(@Query("user_id") user_id: String?): Call<WebResponse<Any?>?>?


    @FormUrlEncoded
    @POST("forgot_password")
    open fun forgetpass(@Field("user_email") user_email: String?): Call<WebResponse<Any?>?>?


    @PUT("update_password")
    open fun changepass(
        @Query("user_id") user_id: String?,
        @Query("user_old_password") user_old_password: String?,
        @Query("user_new_password") user_new_password: String?
    ): Call<WebResponse<Any?>?>?

    @PUT("update_password")
    open fun changenewpass(
        @Query("user_id") user_id: String?,
        @Query("user_new_password") user_new_password: String?
    ): Call<WebResponse<Any?>?>?


    @PUT("task/task_status")
    open fun taskclaim(
        @Query("ts_id") user_id: String?,
        @Query("ts_reward") user_new_password: String?
    ): Call<WebResponse<Any?>?>?


    @PUT("task/task_status?ts_id")
    open fun ts_id(
        @Query("ts_id") user_id: String?,
        @Query("ts_reward") user_new_password: String?
    ): Call<WebResponse<Any?>?>?


    @PUT("worksheet/worksheet_status")
    open fun ws_id(
        @Query("ws_id") user_id: String?,
        @Query("ws_reward") user_new_password: String?
    ): Call<WebResponse<Any?>?>?




    @FormUrlEncoded
    @POST("device_create")
    open fun device_create(
        @Field("device_user_id") user_email: String?,
        @Field("device_child_id") device_child_id: String?,
        @Field("device_title") user_password: String?
    ): Call<WebResponse<Any?>?>?


    @FormUrlEncoded
    @POST("task/task_create")
    open fun task_create(
        @Field("task_user_id") task_user_id: String?,
        @Field("task_device_id") task_device_id: String?,
        @Field("task_description") task_description: String?,
        @Field("task_frequency") task_frequency: String?,
        @Field("task_start_date") task_start_date: String?,
        @Field("task_end_date") task_end_date: String?,
        @Field("task_reward") task_reward: String?,
        @Field("task_json_object") task_json_object: JSONObject?


    ): Call<WebResponse<Any?>?>?







    @FormUrlEncoded
    @POST("worksheet/worksheet_task_create")
    open fun worksheet_task_create(
        @Field("wt_user_id") wt_user_id: String?,
        @Field("wt_device_id") wt_device_id: String?,
        @Field("wt_subject_id") wt_subject_id: String?,
        @Field("wt_description") wt_description: String?,
        @Field("wt_start_date") wt_start_date: String?,
        @Field("wt_end_date") wt_end_date: String?,
        @Field("wt_reward") wt_reward: String?
    ): Call<WebResponse<Any?>?>?


    @FormUrlEncoded
    @POST("worksheet/child/worksheet_task_submition")
    open fun submmittask(
        @Field("ws_wt_id") wt_user_id: String?,
        @Field("ws_device_id") wt_device_id: String, @Field("ws_answer") ws_answer: JSONObject
    ): Call<WebResponse<Any?>?>?

    @Multipart
    @POST("task/child/task_submition")
    open fun task_submit(
        @Part("ts_device_id") task_user_id: RequestBody?,
        @Part("ts_task_id") task_device_id: RequestBody?,
        @Part("ts_date") task_description: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Call<WebResponse<Any?>?>?


    @PUT("task/task_status")
    open fun changestatus(
        @Query("ts_id") user_id: String?,
        @Query("ts_status") user_new_password: String?
    ): Call<WebResponse<Any?>?>?


    @FormUrlEncoded
    @POST("smart_schdelue/schdelue_create")
    open fun smartsch_create(
        @Field("ss_device_id") task_user_id: String?,
        @Field("ss_title") task_device_id: String?,
        @Field("ss_day") task_description: String?,
        @Field("ss_start_time") task_frequency: String?,
        @Field("ss_end_time") task_start_date: String?,
    ): Call<WebResponse<Any?>?>?


    @FormUrlEncoded
    @POST("device_update")
    open fun addplaces(
        @Field("device_id") device_id: String?,
        @Field("device_latitude") device_latitude: String?,
        @Field("device_longitude") device_longitude: String?,
        @Field("device_radius") device_radius: String?
    ): Call<WebResponse<Any?>?>?


    @FormUrlEncoded
    @POST("device_coordinate/coordinate_create")
    open fun addgeofences(
        @Field("dc_device_id") dc_device_id: String?,
        @Field("dc_title") dc_title: String?,
        @Field("dc_address") dc_address: String?,
        @Field("dc_latitude") dc_latitude: String?,
        @Field("dc_longitude") dc_longitude: String?,
        @Field("dc_radius") dc_radius: String?
    ): Call<WebResponse<Any?>?>?


    @DELETE("task/task_delete")
    open fun task_delete(@Query("task_id") dc_device_id: String?): Call<WebResponse<Any?>?>?


    @DELETE("smart_schdelue/schdelue_delete")
    open fun deletdata(@Query("ss_id") dc_device_id: String?): Call<WebResponse<Any?>?>?







    @DELETE("device_coordinate/coordinate_delete")
    open fun deletgeo(@Query("dc_id") dc_device_id: String?): Call<WebResponse<Any?>?>?


//
//    @Multipart
//    @POST("verification_code")
//    open fun getverificaton(@Part("user_id") email: RequestBody?,
//                            @Part("verification_code") password: RequestBody?,
//                            @Part("user_lang") device_type: RequestBody?): Call<WebResponse<UserWrapper>>?
//
//    @GET("resend_code")
//    open fun getverificaton(@Query("user_id") email: String?,
//                            @Query("user_lang") device_type: String?): Call<WebResponse<UserWrapper>>?
//
//    @GET("content")
//    open fun content(@Query("type") email: String?,
//                            @Query("user_lang") device_type: String?): Call<WebResponse<UserWrapper>>?
//
//    @Multipart
//    @POST("forgot_password")
//    open fun forget(@Part("user_email") email: RequestBody?,
//                    @Part("user_lang") device_type: RequestBody?): Call<WebResponse<UserWrapper>>?
//
//    @Multipart
//    @POST("change_password")
//    open fun Chang_pass(@Part("user_id") user_id: RequestBody?,
//                        @Part("old_password") old_password: RequestBody?,
//                        @Part("new_password") new_password: RequestBody?,
//                        @Part("user_lang") user_lang: RequestBody?): Call<WebResponse<UserWrapper>>?
//
//    @Multipart
//    @POST("change_password")
//    open fun verify_cahnge_password(@Part("user_id") user_id: RequestBody?,
//                                    @Part("new_password") new_password: RequestBody?,
//                                    @Part("user_lang") user_lang: RequestBody?): Call<WebResponse<UserWrapper>>?
//
//
//
//
//    @FormUrlEncoded
//    @POST("create_trainer_address")
//    fun get_create_address(@Part("user_id") user_id: RequestBody?,
//                           @Part("ua_name") ua_name: RequestBody?,
//                           @Part("ua_recipient_name") ua_recipient_name: RequestBody?,
//                           @Part("ua_contact_code") ua_contact_code: RequestBody?,
//                           @Part("ua_contact_number") ua_contact_number: RequestBody?,
//                           @Part("ua_state") ua_state: RequestBody?,
//                           @Part("ua_is_city") ua_is_city: RequestBody?,
//                           @Part("ua_address") ua_address: RequestBody?,
//                           @Part("user_lang") lang: RequestBody?): Call<WebData>?
//
//
//
//    @GET("gallery_list")
//    fun getImagesprofile(@Query("user_id") userId: String?,
//                  @Query("other_id") follow_type: String?,
//                  @Query("user_lang") lang: String?): Call<WebResponse<List<Profileimages?>?>?>?
//
//
//
//    @GET("calender_class")
//    fun getcalander(@Query("user_id") userId: String?,
//                         @Query("date") date: String?,
//                         @Query("type") type: String?,
//                         @Query("user_lang") lang: String?): Call<WebResponse<List<Calandermodel?>?>?>?
//
//
//    @GET("notification_list")
//    fun getnotification(@Query("user_id") userId: String?,
//                        @Query("user_lang") lang: String?): Call<WebResponse<List<Notificationmodel?>?>?>?
//
//
//    @DELETE("notification_delete")
//    fun delete_notification(@Query("user_id") user_id: String?, @Query("notification_id") notification_id: String, @Query("user_lang") user_lang: String?): Call<WebData>?
//
//
//    @Multipart
//    @POST("user_stripe")
//    fun srip_account(@Part("user_id") user_id: RequestBody?,
//                     @Part("user_stripe_acc") ua_state: RequestBody?,
//                     @Part("user_lang") lang: RequestBody?): Call<WebResponse<UserWrapper>>?
//
//
//
//    @POST("rates")
//    fun getAmount(@Body jsonBody: JSONObject?): Call<ResponseBody>?
//
//    @GET("user_graph")
//    fun getUserGraph(@Query("user_id") email: String?,
//    @Query("user_lang") device_type: String?): Call<GraphData>
//
//
//
//
//
//    @GET("user_tag_list")
//    fun getUserList(@Query("user_id") email: String?,
//                    @Query("user_name") name: String?,
//                     @Query("user_lang") device_type: String?): Call<TagList>
//
//
//
//    @Multipart
//    @POST("create_gallery")
//    fun add_profileimage(@Part("ug_user_id") ug_user_id: RequestBody?, @Part("user_lang") lang: RequestBody?,@Part files: List<MultipartBody.Part>?): Call<WebData?>
//
//
//
//
//    @DELETE("delete_gallery_image")
//    fun delete_certificate(@Query("user_id") user_id: String?, @Query("ug_id") ug_id: JSONArray, @Query("user_lang") user_lang: String?): Call<WebData>?
//
//
//
//
//
//
//
//    @GET("trainer_stats")
//    fun getdatauser(@Query("user_id") userId: String?,
//                    @Query("stats_type") type: String?,
//                    @Query("user_lang") lang: String?): Call<WebResponse<Usermodelstats?>?>?


//    @FormUrlEncoded
//    @POST("purchase")
//    open fun dataset(@Part("user_id") email: String?,
//                   @Part("package_id") package_id: String?,
//                  @Part("purchase_token") purchase_token: String?,
//                  @Part("user_type") purchase_token: String?,
//                  @Part("user_lang") device_type: String?): Call<WebResponse?>?
}