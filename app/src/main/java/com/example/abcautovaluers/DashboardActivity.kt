package com.example.abcautovaluers

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_login.*


class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val userSession = SessionManager(this)
        val valuationInstance = ValuationInstance(this)

        val email = userSession.userEmail

        textMemberName.text = getString(R.string.welcome, email)

        if (valuationInstance.checkValuation()){

            buttonNew.text = getString(R.string.proceed)

        }

        buttonNew.setOnClickListener {

            startActivity(Intent(this, ValuationActivity::class.java))

        }

        buttonLogout.setOnClickListener {

            userSession.logoutUser()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }


    }


}