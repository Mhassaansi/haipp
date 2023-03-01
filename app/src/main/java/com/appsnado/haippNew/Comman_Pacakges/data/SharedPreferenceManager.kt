package com.appsnado.haippNew.data

import android.content.Context
import android.preference.PreferenceManager
import com.appsnado.haippNew.Comman_Pacakges.data.UserWrapper
import com.google.gson.Gson


class SharedPreferenceManager(private val context: Context?) : BasePreferenceHelper() {

    fun putUser(entity: UserWrapper?) {
        putStringPreference(context, KEY_FILENAME, KEY_USER, Gson().toJson(entity))
    }

    fun getUser(): UserWrapper? {
        val keyData = getStringPreference(context, KEY_FILENAME, KEY_USER)
        return Gson().fromJson(keyData, UserWrapper::class.java)
    }

    fun removeUser() {
        removePreference(context, KEY_FILENAME, KEY_USER)
    }

    fun putIsSecondRun(isExecuted: Boolean) {
        putBooleanPreference(context, KEY_FILENAME, KEY_IS_FIRST_RUN, isExecuted)
    }

    fun isSecondRun(): Boolean {
        return getBooleanPreference(context, KEY_FILENAME, KEY_IS_FIRST_RUN)
    }

    fun setTeam(isTeam: Boolean) {
        putBooleanPreference(context, KEY_FILENAME, KEY_IS_TEAM, isTeam)
    }

    fun isTeam(): Boolean {
        return getBooleanPreference(context, KEY_FILENAME, KEY_IS_TEAM)
    }

    fun setToken(token: String?) {
        putStringPreference(context, KEY_FILENAME, KEY_TOKEN, token)
    }


    fun setauthspotify(token: String?) {
        putStringPreference(context, KEY_FILENAME, Spotify, token)
    }
    fun getspotify(): String? {
        return getStringPreference(context, KEY_FILENAME, Spotify)
    }




    fun getToken(): String? {
        return getStringPreference(context, KEY_FILENAME, KEY_TOKEN)
    }

    fun setDeviceToken(deviceToken: String?) {
        putStringPreference(context, KEY_FILENAME, DEVICE_TOKEN, deviceToken)
    }

    fun getDeviceToken(): String? {
        return getStringPreference(context, KEY_FILENAME, DEVICE_TOKEN)
    }

    fun setIsLogin(isLogout: Boolean) {
        putBooleanPreference(context, KEY_FILENAME, IS_LOGIN, isLogout)
    }

    fun setuserid(userid: String?) {
        putStringPreference(context, KEY_FILENAME, USERID, userid)
    }




    fun settype(userid: String?) {
        putStringPreference(context, KEY_FILENAME, usertypedefault, userid)
    }
    fun gettype(): String? {
        return getStringPreference(context, KEY_FILENAME, usertypedefault)
    }

    fun setlockscreen(s: Boolean?) {
        putBooleanPreference(context, KEY_FILENAME, LOCKSCREEN, s!!)
    }
    fun getlockscreen(): Boolean? {
        return getBooleanPreference(context, KEY_FILENAME, LOCKSCREEN)
    }
    fun setdevicesid(userid: String?) {
        putStringPreference(context, KEY_FILENAME, DEVCICEID, userid)
    }
    fun getdevicesid(): String? {
        return getStringPreference(context, KEY_FILENAME, DEVCICEID)
    }

    fun setdevicetitle(Title: String?) {
        putStringPreference(context, KEY_FILENAME, DEVICETITLE, Title)
    }
    fun getdevicestitle(): String? {
        return getStringPreference(context, KEY_FILENAME, DEVICETITLE)
    }


    fun setscreenlock(t: Boolean) {
        putBooleanPreference(context, KEY_FILENAME, SCREENLOCK, t)
    }
    fun getscreenlock(): Boolean? {
        return getBooleanPreference(context, KEY_FILENAME, SCREENLOCK)
    }
//
    fun setsmartschlock(t: Boolean) {
        putBooleanPreference(context, KEY_FILENAME, SMARTLOCK, t)
    }
    fun getsmarkschlock(): Boolean? {
        return getBooleanPreference(context, KEY_FILENAME, SMARTLOCK)
    }

    //

    fun gettimeuse(): String? {
        return getStringPreference(context, KEY_FILENAME, TIMESET)
    }

    fun settimeuse(Title: String?) {
        putStringPreference(context, KEY_FILENAME, TIMESET, Title)
    }

    fun setsmartsch(t: Boolean) {
        putBooleanPreference(context, KEY_FILENAME, SMARTSCH, t)
    }



    fun setnotification(time: String) {
        putStringPreference(context, KEY_FILENAME, NOTIFICATIONDATA, time)
    }
//    fun getnotification(time: String) {
//        putStringPreference(context, KEY_FILENAME, NOTIFICATIONDATA, time)
//    }

    fun getnotification() : String? {
        return getStringPreference(context, KEY_FILENAME, NOTIFICATIONDATA)
    }


    fun gettimecount() : String? {
        return getStringPreference(context, KEY_FILENAME, TIMECOUNT)
    }


    fun getsmartsch(): Boolean? {
        return getBooleanPreference(context, KEY_FILENAME, SMARTSCH)
    }



    fun setchildlogout(t: Boolean) {
        putBooleanPreference(context, KEY_FILENAME, CHILDLOGOUT, t)
    }
    fun getchildlogout(): Boolean? {
        return getBooleanPreference(context, KEY_FILENAME, CHILDLOGOUT)
    }

////

    fun setstarttime(t: Boolean) {
        putBooleanPreference(context, KEY_FILENAME, STARTTIME, t)
    }
    fun getstarttime(): Boolean? {
        return getBooleanPreference(context, KEY_FILENAME, STARTTIME)
    }



    fun setworksheernav(t: Boolean) {
        putBooleanPreference(context, KEY_FILENAME, WORKNAV, t)
    }
    fun getworksheetnav(): Boolean? {
        return getBooleanPreference(context, KEY_FILENAME, WORKNAV)
    }
    fun settasksheernav(t: Boolean) {
        putBooleanPreference(context, KEY_FILENAME, TASKNAV, t)
    }
    fun gettasksheetnav(): Boolean? {
        return getBooleanPreference(context, KEY_FILENAME, TASKNAV)
    }


    fun setAppupdate(t: Boolean) {
        putBooleanPreference(context, KEY_FILENAME, UPDATEAPP, t)
    }
    fun getAppupdate(): Boolean? {
        return getBooleanPreference(context, KEY_FILENAME, UPDATEAPP)
    }


    fun setJsonupdate(t: Boolean) {
        putBooleanPreference(context, KEY_FILENAME, UPDATEAPP, t)
    }
    fun getJsonupdate(): Boolean? {
        return getBooleanPreference(context, KEY_FILENAME, UPDATEAPP)
    }





    fun setfirsttimservices(t: Boolean) {
        putBooleanPreference(context, KEY_FILENAME, FIRSTTIMESERVICS, t)
    }
    fun getfirsttimeservices(): Boolean? {
        return getBooleanPreference(context, KEY_FILENAME, FIRSTTIMESERVICS)
    }

    ///
    fun setchildref(Title: String?) {
        putStringPreference(context, KEY_FILENAME, DatabaseReference, Title)
    }
    fun getchildref(): String? {
        return getStringPreference(context, KEY_FILENAME, DatabaseReference)
    }

    ////

    fun getuserid(): String? {
        return getStringPreference(context, KEY_FILENAME, USERID)
    }

    fun setlanguage(code: String?) {
        putStringPreference(context, KEY_FILENAME, LANGUAGE, code)
    }

    fun set_fitbittoken(code: String?) {
        putStringPreference(context, KEY_FILENAME, KEY_Access_token_fitbit, code)
    }
    fun get_fitbit(): String? {
        return getStringPreference(context, KEY_FILENAME, KEY_Access_token_fitbit)
    }
    fun getlang(): String? {
        return getStringPreference(context, KEY_FILENAME, LANGUAGE)
    }

    fun isLogin(): Boolean {
        return getBooleanPreference(context, KEY_FILENAME, IS_LOGIN)
    }

    fun setIsProfileComplete(isProfileComplete: Boolean) {
        putBooleanPreference(context, KEY_FILENAME, IS_PROFILE_COMPLETE, isProfileComplete)
    }

    fun isProfileComplete(): Boolean {
        return getBooleanPreference(context, KEY_FILENAME, IS_PROFILE_COMPLETE)
    }





    fun settimecount(time: String) {
        putStringPreference(context, KEY_FILENAME, TIMECOUNT, time)
    }





    fun setDefaultValues() {
        putStringPreference(context, KEY_FILENAME, KEY_USER, null)
        putStringPreference(context, KEY_FILENAME, KEY_TOKEN, null)
        putStringPreference(context, KEY_FILENAME, DEVICE_TOKEN, null)
        putBooleanPreference(context, KEY_FILENAME, IS_LOGIN, false)
    }

    companion object {
        private val KEY_FILENAME: String? = "file_app_key_Karve"
        private val KEY_IS_TEAM: String? = "key_team"
        private val KEY_TOKEN: String? = "key_token"
        private val Spotify: String? = "SPOTIFY_QA"
        private val DEVICE_TOKEN: String? = "key_token"
        private val KEY_USER: String? = "keydata_user_Karve"
        private val KEY_IS_FIRST_RUN: String? = "keydata_first_run"
        private val IS_LOGIN: String? = "is_logout"
        private val IS_PROFILE_COMPLETE: String? = "is_profile_complete"
        private val LANGUAGE: String? = "language"
        private val USERID: String? = "userid"
        private val usertypedefault: String? = "usertype"
        private val KEY_Access_token_fitbit: String? = "fitbit"
        private val DEVCICEID: String? = "deviceid"
        private val KEY_SPOTIFY: String? = "Spotify"
        private val LOCKSCREEN: String? = "LOCK"
        private val DEVICETITLE: String? = "DEVICETITLE"
        private val SCREENLOCK: String? = "SCREENLOCK"
        private val SMARTSCH: String? = "SMARTSCH"
        private val CHILDLOGOUT: String? = "CHILDLOGOUT"

        private val DatabaseReference: String? = "DatabaseReference"
        private val UPDATEAPP: String? = "UPDATEAPP"
        private val JSONUPDATEAPP: String? = "JSONUPDATEAPP"

        private val TIMESET: String? = "TIMESET"

        private val TIMECOUNT: String? = "TIMECOUNT"


        private val STARTTIME: String? = "STARTTIME"
        private val SMARTLOCK: String? = "SMARTLOCK"

        private val WORKNAV: String? = "WORKNAV"
        private val TASKNAV: String? = "TASKNAV"

        private val NOTIFICATIONDATA: String? = "NOTIFICATIONDATA"

        private val FIRSTTIMESERVICS: String? = "FIRSTTIMESERVICS"
    }

}