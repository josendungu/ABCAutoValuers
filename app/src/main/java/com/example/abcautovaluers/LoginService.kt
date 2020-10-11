package com.example.abcautovaluers

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import android.widget.Toast
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class LoginService: IntentService("Login Service") {

    private val tag = "LoginService"
    private lateinit var resultReceiver: ResultReceiver

    override fun onHandleIntent(p0: Intent?) {

        var result: String? = null

        val email: String = p0?.extras?.get("email") as String
        val password: String = p0.extras?.get("password") as String
        resultReceiver = p0.extras?.get("receiver") as ResultReceiver

        try {

            val data = (URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")
                        + "&&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8"))
            val db = DB_con(data)
            result = db.connection

        } catch (e: UnsupportedEncodingException) {

            e.printStackTrace()

        }

        if (result != null){

            Log.d(tag, "results: $result")

            when (result) {
                RESPONSE_LOGIN_PASSED.toString() -> {

                    val sessionManager = SessionManager(this)
                    sessionManager.addMember(email)

                    val intent = Intent(this, DashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                }
                RESPONSE_PASSWORD_DOES_NOT_MATCH.toString() -> {

                    val bundle = Bundle()
                    bundle.putString(BUNDLE_PASSWORD_DO_NOT_MATCH, "pass_do_not_match")
                    resultReceiver.send(RESPONSE_PASSWORD_DOES_NOT_MATCH, bundle)

                }
                RESPONSE_MEMBER_DOES_NOT_EXIST.toString() -> {

                    val bundle = Bundle()
                    bundle.putString(BUNDLE_MEMBER_DOES_NOT_EXIST, "member_does_not_exist")
                    resultReceiver.send(RESPONSE_MEMBER_DOES_NOT_EXIST, bundle)

                }
            }

        }

    }
}