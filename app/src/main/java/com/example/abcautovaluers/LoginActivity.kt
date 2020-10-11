package com.example.abcautovaluers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.ActionBarContainer
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer {

            if (it){

                connection_state.visibility = View.VISIBLE
                connection_state.text = getString(R.string.connected)
                connection_state.setBackgroundColor(resources.getColor(R.color.colorSuccess))

                Handler().postDelayed({

                    connection_state.visibility = View.INVISIBLE

                }, 10000)

            } else {

                connection_state.visibility = View.VISIBLE
                connection_state.text = getString(R.string.not_connected)
                connection_state.setBackgroundColor(resources.getColor(R.color.colorPrimary))

            }
        })

        val resultReceiver =MyResultReceiver(handler)

        buttonSubmit.setOnClickListener {

            et_email.error = null
            et_password.error = null

            email = et_email.editText?.text.toString()
            password = et_password.editText?.text.toString()

            if (email.isNotEmpty()){

                if (password.isNotEmpty()){

                    val intent = Intent(this, LoginService::class.java)
                    intent.putExtra("email", email)
                    intent.putExtra("password", password)
                    intent.putExtra("receiver", resultReceiver)
                    startService(intent)

                } else {

                    et_password.error = "Field can not be empty."

                }
            } else {

                et_email.error = "Field can not be empty."

            }

        }
    }

    private inner class MyResultReceiver(handler: Handler?) : ResultReceiver(handler) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            super.onReceiveResult(resultCode, resultData)

            when(resultCode){

                RESPONSE_MEMBER_DOES_NOT_EXIST -> {

                    Snackbar.make(snackBar_cont, "There is no member registered with the email address entered! Please check the email address and try again", Snackbar.LENGTH_LONG).show()

                }

                RESPONSE_PASSWORD_DOES_NOT_MATCH -> {

                    Snackbar.make(snackBar_cont, "Passwords do not match! Please check the password entered and try again", Snackbar.LENGTH_LONG).show()
                }

            }
        }
    }
}