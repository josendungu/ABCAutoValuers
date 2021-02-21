package com.example.abcautovaluers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_dashboard.*


class DashboardActivity : AppCompatActivity() {

    private lateinit var mUser: User

    companion object{
        const val USER_REFERENCE = "user_reference"
        const val USER_ADDED = "user_added"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val userSession = SessionManager(this)
        val valuationInstance = ValuationInstance(this)

        checkUserAdded()

        if (userSession.checkLoginState()){

            mUser = userSession.userDetails

        } else {

            mUser = intent.getParcelableExtra(USER_REFERENCE) as User
            userSession.setLoggedState(true)

        }

        textMemberName.text = getString(R.string.welcome, mUser.email)

        buttonNew.setOnClickListener {

            if (valuationInstance.checkValuation()){

                PopulateAlert(KEY_VALUATION_ALERT, this)

            } else {

                val intent = Intent(this, ValuationActivity::class.java)
                startActivity(intent)

            }

        }

        buttonNewUser.setOnClickListener {

            val intent = Intent(this, UserViewActivity::class.java)
            startActivity(intent)

        }

        buttonLogout.setOnClickListener {

            userSession.logoutUser()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }

    }

    private fun checkUserAdded() {

        val userAdded = intent.extras?.get(USER_ADDED) as Boolean

        if (userAdded){

            Snackbar.make(snackViewCont, "User was successfully added", Snackbar.LENGTH_LONG).show()

        }

    }

}