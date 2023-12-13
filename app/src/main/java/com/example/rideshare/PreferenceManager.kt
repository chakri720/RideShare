package com.example.rideshare

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(private val context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "MyAppPreferences"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    }

    var isLoggedIn: Boolean
        get() = preferences.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = preferences.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()
}