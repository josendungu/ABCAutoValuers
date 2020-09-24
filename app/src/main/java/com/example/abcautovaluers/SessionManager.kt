package com.example.abcautovaluers

import android.content.Context
import android.content.SharedPreferences

class SessionManager(val context: Context) {

    private var userSession: SharedPreferences = context.getSharedPreferences(
        "userLoginSession",
        Context.MODE_PRIVATE
    )
    private var editor: SharedPreferences.Editor = userSession.edit()

    private val KEY_IS_LOGGED_IN = "isLoggedIn"
    private val KEY_REMEMBER_LOGIN_STATUS = "login-details"

    @JvmField
    val KEY_EMAIL: String = "email"
    @JvmField
    val KEY_USERNAME: String = "username"

    public fun getUserData(): HashMap<String, String?>{

        var defaultSet = false

        val userData = HashMap<String, String?>()

        userData[KEY_EMAIL] = userSession.getString(KEY_EMAIL, null)
        userData[KEY_USERNAME] = userSession.getString(KEY_EMAIL, null)

        return userData
    }

}