package com.appsnado.haippNew.data

import android.app.Activity
import android.content.Context

open class BasePreferenceHelper {
    protected fun putStringPreference(context: Context?, prefsName: String?, key: String?, value: String?) {
        val preferences = context!!.getSharedPreferences(prefsName, Activity.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    protected fun getStringPreference(context: Context?, prefsName: String?, key: String?): String? {
        val preferences = context!!.getSharedPreferences(prefsName, Activity.MODE_PRIVATE)
        return preferences.getString(key, "")
    }

    protected fun putBooleanPreference(context: Context?, prefsName: String?, key: String?, value: Boolean) {
        val preferences = context!!.getSharedPreferences(prefsName, Activity.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    protected fun getBooleanPreference(context: Context?, prefsName: String?, key: String?): Boolean {
        val preferences = context!!.getSharedPreferences(prefsName, Activity.MODE_PRIVATE)
        return preferences.getBoolean(key, false)
    }

    protected fun putIntegerPreference(context: Context?, prefsName: String?, key: String?, value: Int) {
        val preferences = context!!.getSharedPreferences(prefsName, Activity.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    /**
     * Get a integer under a particular key and filename
     *
     * @param context the filename of preferences
     * @param key     name of preference
     * @return -1 if key is not found
     */
    protected fun getIntegerPreference(context: Context?, prefsName: String?, key: String?): Int {
        val preferences = context!!.getSharedPreferences(prefsName, Activity.MODE_PRIVATE)
        return preferences.getInt(key, -1)
    }

    protected fun putLongPreference(context: Context?, prefsName: String?, key: String?, value: Long) {
        val preferences = context!!.getSharedPreferences(prefsName, Activity.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    /**
     * Get a integer under a particular key and filename
     *
     * @param context the filename of preferences
     * @param key     name of preference
     * @return Integer.MIN if key is not found
     */
    protected fun getLongPreference(context: Context?, prefsName: String?, key: String?): Long {
        val preferences = context!!.getSharedPreferences(prefsName, Activity.MODE_PRIVATE)
        return preferences.getLong(key, Int.MIN_VALUE.toLong())
    }

    protected fun putFloatPreference(context: Context?, prefsName: String?, key: String?, value: Float) {
        val preferences = context!!.getSharedPreferences(prefsName, Activity.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    protected fun getFloatPreference(context: Context?, prefsName: String?, key: String?): Float {
        val preferences = context!!.getSharedPreferences(prefsName, Activity.MODE_PRIVATE)
        return preferences.getFloat(key, Float.MIN_VALUE)
    }

    protected fun removePreference(context: Context?, prefsName: String?, key: String?) {
        val preferences = context!!.getSharedPreferences(prefsName, Activity.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.remove(key)
        editor.apply()
    }
}