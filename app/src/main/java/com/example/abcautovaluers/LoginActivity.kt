package com.example.abcautovaluers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String

    override fun onResume() {
        super.onResume()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonSubmit.setOnClickListener {

            startActivity(Intent(this, DashboardActivity::class.java))

            email = et_email.editText?.text.toString()
            password = et_password.editText?.text.toString()

            loginUser()

        }
    }

    private fun loginUser() {



    }
}