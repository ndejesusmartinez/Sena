package com.projectSena.sena.utils

import android.content.Context
import android.content.SharedPreferences

class mySharedPreference {
    private val fileName: String = "MY_SHARED_FILENAME"

    fun removeAccessToken(contex: Context){
        val preference: SharedPreferences = contex.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        preference.edit().remove("accessToken").apply()
    }

    fun getAccessToken(contex: Context): String{
        val preference: SharedPreferences = contex.getSharedPreferences(fileName,Context.MODE_PRIVATE)
        return preference.getString("accesstoken","").toString()
    }

    fun setAccessToken(contex: Context, accessToken: String){
        val preferences = contex.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        preferences.edit().putString("accessToken",accessToken).apply()
    }

}