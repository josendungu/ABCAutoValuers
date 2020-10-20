package com.example.abcautovaluers

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_login.*


class DashboardActivity : AppCompatActivity() {

    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val userSession = SessionManager(this)
        val valuationInstance = ValuationInstance(this)



        email = if (userSession.checkSessionState()){

            userSession.userEmail

        } else {

            intent.extras?.getString("email")

        }

        textMemberName.text = getString(R.string.welcome, email)



        buttonNew.setOnClickListener {

            if (valuationInstance.checkValuation()){

                PopulateAlert(KEY_VALUATION_ALERT, this)

            } else {

                val intent = Intent(this, ValuationActivity::class.java)
                startActivity(intent)

            }

        }

        buttonLogout.setOnClickListener {

            userSession.logoutUser()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }


    }


}