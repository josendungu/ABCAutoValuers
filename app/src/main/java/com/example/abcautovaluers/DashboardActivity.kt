package com.example.abcautovaluers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_schedule_list.*


class DashboardActivity : AppCompatActivity() {

    private lateinit var mUser: User

    companion object {
        const val USER_REFERENCE = "user_reference"
        const val USER_ADDED = "user_added"
        const val VALUATION_DELETED = "valuation_deleted"
        const val ASSIGNED = "assigned"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val userSession = SessionManager(this)
        val valuationInstance = ValuationInstance(this)

        checkUserAdded()
        checkValuationDeleted()
        checkValuationAssigned()

        mUser = if (userSession.checkLoginState()) {

            userSession.userDetails

        } else {

            intent.getParcelableExtra(USER_REFERENCE) as User

        }


        userSession.setLoggedState(true)

        completedValuations.setOnClickListener {
            if (mUser.admin!!) {

                val intent = Intent(this, CompletedListActivity::class.java)
                startActivity(intent)

            } else {

                Snackbar.make(
                    snackViewCont,
                    "You do not have permission to view completed valuations",
                    Snackbar.LENGTH_LONG
                ).show()

            }
        }

        addUser.setOnClickListener {

            if (mUser.admin!!) {

                val intent = Intent(this, UserViewActivity::class.java)
                startActivity(intent)

            } else {

                Snackbar.make(
                    snackViewCont,
                    "You do not have permission to add users",
                    Snackbar.LENGTH_LONG
                ).show()

            }


        }

        scheduled.setOnClickListener {
            val intent = Intent(this, ScheduleListActivity::class.java)
            startActivity(intent)

        }

        textMemberName.text = getString(R.string.welcome, mUser.username)

        valuate.setOnClickListener {

            if (valuationInstance.checkValuation()){
                val intent = Intent(this, ValuationActivity::class.java)
                startActivity(intent)
            } else {
                Snackbar.make(snackViewCont, "You don't have an active valuation. Please check for assigned valuation in schedule tab ", Snackbar.LENGTH_LONG).show()
            }

        }

        logout.setOnClickListener {

            userSession.logoutUser()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }

    }

    private fun checkValuationDeleted() {
        val deleted = intent.extras?.get(VALUATION_DELETED) as Boolean

        if (deleted) {

            Snackbar.make(
                snackViewCont,
                "Valuation was successfully marked as complete",
                Snackbar.LENGTH_LONG
            ).show()

        }
    }


    private fun checkValuationAssigned() {
        val assigned = intent.extras?.get(ASSIGNED) as Boolean

        if (assigned) {

            Snackbar.make(
                snackViewCont,
                "Valuation was successfully assigned",
                Snackbar.LENGTH_LONG
            ).show()

        }
    }

    private fun checkUserAdded() {

        val userAdded = intent.extras?.get(USER_ADDED) as Boolean

        if (userAdded) {

            Snackbar.make(snackViewCont, "User was successfully added", Snackbar.LENGTH_LONG).show()

        }

    }

}


