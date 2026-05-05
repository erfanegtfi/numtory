package com.numtory.application.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import java.util.ArrayList

class Session(context: Context) {

    companion object {
        private const val SHARED_PREFS_NAME = "prefs"
    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    private fun getPreferences(): SharedPreferences = preferences

    private fun getPreferencesEditor(): SharedPreferences.Editor = preferences.edit()

    fun getPreferenceValue(key: String, defValue: String?): String? =
        getPreferences().getString(key, defValue) ?: defValue

    fun getPreferenceValue(key: String, defValue: Int): Int =
        getPreferences().getInt(key, defValue)

    fun getPreferenceValue(key: String, defValue: Long): Long =
        getPreferences().getLong(key, defValue)

    fun getPreferenceValue(key: String, defValue: Boolean): Boolean =
        getPreferences().getBoolean(key, defValue)

    fun setPreferenceValue(key: String, prefsValue: String?) {
        getPreferencesEditor().putString(key, prefsValue).apply()
    }

    fun setPreferenceValue(key: String, prefsValue: Int) {
        getPreferencesEditor().putInt(key, prefsValue).apply()
    }

    fun setPreferenceValue(key: String, prefsValue: Long) {
        getPreferencesEditor().putLong(key, prefsValue).apply()
    }

    fun setPreferenceValue(key: String, prefsValue: Boolean) {
        getPreferencesEditor().putBoolean(key, prefsValue).apply()
    }

    fun containsPreferenceKey(key: String): Boolean =
        getPreferences().contains(key)

    fun <T> getArrayObject(key: String, generic: Class<Array<T>>): ArrayList<T>? {
        val gson = Gson()
        val json = getPreferences().getString(key, "") ?: ""
        val list: Array<T>? = gson.fromJson(json, generic)
        return if (list != null) ArrayList(listOf(*list)) else null
    }

    fun <T> getObject(key: String, generic: Class<T>): T? {
        val gson = Gson()
        val json = getPreferences().getString(key, "") ?: ""
        return gson.fromJson(json, generic)
    }

    fun <T> setObject(key: String, value: T) {
        val gson = Gson()
        val json = gson.toJson(value)
        getPreferencesEditor().putString(key, json).apply()
    }

    fun removePreferenceValue(key: String) {
        getPreferencesEditor().remove(key).apply()
    }

    fun clear() {
        getPreferencesEditor().clear().apply()
    }
}