package com.example.abcautovaluers

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

        mUser = if (userSession.checkLoginState()){

            userSession.userDetails

        } else {

            intent.getParcelableExtra(USER_REFERENCE) as User

        }


        userSession.setLoggedState(true)

        addUser.setOnClickListener {

            if (mUser.admin!!){

                val intent = Intent(this, UserViewActivity::class.java)
                startActivity(intent)

            } else {

                Snackbar.make(snackViewCont, "You do not have permission to add users", Snackbar.LENGTH_LONG).show()

            }


        }

        scheduled.setOnClickListener{
            val intent = Intent(this, ScheduleListActivity::class.java)
            startActivity(intent)

        }

        textMemberName.text = getString(R.string.welcome, mUser.username)

        valuate.setOnClickListener {

            if (valuationInstance.checkValuation()){

                PopulateAlert(KEY_VALUATION_ALERT, this)

            } else {

                val intent = Intent(this, ValuationActivity::class.java)
                startActivity(intent)

            }

        }

        logout.setOnClickListener {

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


