package com.appsnado.haippNew.Chat

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ServerTimestamp
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FirebasetimeClass {

    @Expose
    @SerializedName("content")
    private var content: String? = null

    @Expose
    @SerializedName("otherUserID")
    private var otherUserID: String? = null

    @Expose
    @SerializedName("created")
    private var created: String? = null

    @Expose
    @SerializedName("id")
    private var id: String? = null

    @Expose
    @SerializedName("senderID")
    private var SenderID: String? = null

    @Expose
    @SerializedName("senderImage")
    private var SenderImage: String? = null


    @Expose
    @SerializedName("senderName")
    private var SenderName: String? = null


    @Expose
    @SerializedName("otherName")
    private var otherName: String? = null



    @Expose
    @SerializedName("usertype")
    private var usertype: String? = null





    @Expose
    @SerializedName("isSeen")
    private  var isSeen: Boolean? = null


    @Expose
    @SerializedName("firebasetime")
    private var firebasetime: Timestamp? = null


    fun getfirebasetime(): Timestamp? {
        return firebasetime
    }

    fun setfirebasetime(content: Timestamp?) {
        this.firebasetime = content
    }

    fun getusertype(): String? {
        return usertype
    }

    fun setusertype(content: String?) {
        this.usertype = content
    }

    fun getothername(): String? {
        return otherName
    }

    fun setothername(content: String?) {
        this.otherName = content
    }


    fun getContent(): String? {
        return content
    }

    fun setContent(content: String?) {
        this.content = content
    }

    fun getotheruserid(): String? {
        return otherUserID
    }

    fun setotheruserid(mediaurl: String?) {
        this.otherUserID = mediaurl
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getSenderID(): String? {
        return SenderID
    }

    fun setSenderID(senderID: String?) {
        SenderID = senderID
    }

    fun getSenderImage(): String? {
        return SenderImage
    }

    fun setSenderImage(senderImage: String?) {
        SenderImage = senderImage
    }

    fun getSenderName(): String? {
        return SenderName
    }

    fun setSenderName(senderName: String?) {
        SenderName = senderName
    }


    fun getcreated(): String? {
        return created
    }
    fun setCretaednumber(sendercounter: String?) {
        created = sendercounter
    }


    fun getisSeen(): Boolean? {
        return isSeen
    }

    fun setisSeen(isseen: Boolean?) {
        isSeen = isseen
    }

}