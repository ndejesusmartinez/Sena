package com.projectSena.sena.utils

import android.content.Context
import android.content.SharedPreferences

class mySharedPreference {
    private val fileName: String = "MY_SHARED_FILENAME"

    fun getAPIURL(context: Context): String {
        val preference: SharedPreferences =
            context.getSharedPreferences(this.fileName, Context.MODE_PRIVATE)
        return preference.getString("API_URL", "http://192.168.0.103:8050").toString()
    }

    fun setAPIURL(context: Context, apiUrl: String) {
        val preference: SharedPreferences =
            context.getSharedPreferences(this.fileName, Context.MODE_PRIVATE)
        preference.edit().putString("API_URL", apiUrl).apply()
    }

    fun removeAccessToken(contex: Context){
        val preference: SharedPreferences = contex.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        preference.edit().remove("accessToken").apply()
    }

    fun getAccessToken(context: Context): String {
        val preference: SharedPreferences =
            context.getSharedPreferences(this.fileName, Context.MODE_PRIVATE)
        return preference.getString("accessToken", "").toString()
    }

    fun setAccessToken(contex: Context, accessToken: String){
        val preferences: SharedPreferences = contex.getSharedPreferences(this.fileName, Context.MODE_PRIVATE)
        preferences.edit().putString("accessToken",accessToken).apply()
    }

}