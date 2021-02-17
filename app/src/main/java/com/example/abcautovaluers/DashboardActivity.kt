package com.example.abcautovaluers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dashboard.*


class DashboardActivity : AppCompatActivity() {

    companion object{
        const val USER_REFERENCE = "user_reference"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val userSession = SessionManager(this)
        val valuationInstance = ValuationInstance(this)

        if (userSession.checkSessionState()){

            val username: String = userSession.username
            fetchUserDetails(username)

        } else {

            val user = intent.getParcelableExtra(USER_REFERENCE) as User
            loadActivity(user)

        }


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

    private fun loadActivity(user: User){

        textMemberName.text = getString(R.string.welcome, user.email)

    }

    private fun fetchUserDetails(username: String?) {

        val databaseRef = FirebaseUtil.openFirebaseReference("Users")

        var user: User? = null

        if (username != null) {
            databaseRef.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {

                    user = p0.getValue(User::class.java)
                    user?.let { loadActivity(it) }

                }

                override fun onCancelled(p0: DatabaseError) {

                    Log.d("Error", "error occurred")

                }

            })
        }
    }



}