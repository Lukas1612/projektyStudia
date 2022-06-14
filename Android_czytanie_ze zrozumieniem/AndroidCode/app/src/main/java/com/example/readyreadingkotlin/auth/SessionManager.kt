package com.example.readyreadingkotlin.auth

import android.content.Context
import android.content.SharedPreferences
import com.example.readyreadingkotlin.R

class SessionManager (context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)


    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_NAME = "user_name"
        const val USER_PASSWORD = "user_password"
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun saveName(name: String) {
        val editor = prefs.edit()
        editor.putString(USER_NAME, name)
        editor.apply()
    }

    fun savePassword(password: String) {
        val editor = prefs.edit()
        editor.putString(USER_PASSWORD, password)
        editor.apply()
    }
    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun fetchName(): String? {
        return prefs.getString(USER_NAME, null)
    }

    fun fetchPassword(): String? {
        return prefs.getString(USER_PASSWORD, null)
    }


}