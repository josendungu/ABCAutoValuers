package com.example.abcautovaluers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.connection_state
import kotlinx.android.synthetic.main.activity_submitting.*

class LoginActivity : AppCompatActivity() {

    private lateinit var username: String
    private lateinit var password: String

    private val handler = Handler()

    override fun onStart() {
        super.onStart()

        val sessionManager = SessionManager(this)
        if (sessionManager.checkSessionState()) {

            val user = sessionManager.userDetails

            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra(DashboardActivity.USER_REFERENCE, user)
            intent.putExtra(DashboardActivity.USER_ADDED, false)
            intent.putExtra(DashboardActivity.ASSIGNED, false)
            intent.putExtra(DashboardActivity.VALUATION_DELETED, false)

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, {

            if (it) {

                connection_state.visibility = View.VISIBLE
                connection_state.text = getString(R.string.connected)
                connection_state.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorSuccess))

                Handler().postDelayed({

                    connection_state.visibility = View.INVISIBLE

                }, 10000)

            } else {

                connection_state.visibility = View.VISIBLE
                connection_state.text = getString(R.string.not_connected)
                connection_state.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))

            }
        })

        buttonSubmit.setOnClickListener {

            et_username.error = null
            et_password.error = null

            username = et_username.editText?.text.toString()
            password = et_password.editText?.text.toString()

            if (username.isNotEmpty()) {

                if (password.isNotEmpty()) {

                    loginProgress.visibility = View.VISIBLE
                    checkLoginDetails(username)

                } else {

                    et_password.error = "Field can not be empty."

                }
            } else {

                et_username.error = "Field can not be empty."

            }

        }
    }

    private fun moveToDashboard(user: User?) {

        loginProgress.visibility = View.INVISIBLE
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra(DashboardActivity.USER_REFERENCE, user)
        intent.putExtra(DashboardActivity.USER_ADDED, false)
        intent.putExtra(DashboardActivity.ASSIGNED, false)
        intent.putExtra(DashboardActivity.VALUATION_DELETED, false)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }

    private fun validatePassword(user: User?) {

        if (user?.password == password) {

            handleSessionRegistration(user)
            moveToDashboard(user)

        } else {

            et_password.error = "Passwords do not match"
            loginProgress.visibility = View.INVISIBLE

        }
    }

    private fun handleSessionRegistration(user: User?) {

        SessionManager(this).addMember(user, checkBox.isChecked)

    }


    private fun checkLoginDetails(username: String?) {

        val databaseRef = FirebaseUtil.openFirebaseReference("Users")

        var user: User?

        if (username != null) {
            databaseRef.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {

                    if (p0.exists()){

                        user = p0.getValue(User::class.java)

                        if (user != null){

                            validatePassword(user)
                            Log.d("Is user admin", user!!.admin.toString())

                        } else {

                            loginProgress.visibility = View.INVISIBLE
                            PopulateAlert(KEY_ERROR, this@LoginActivity)

                        }

                    } else {

                        et_username.error =  "User does not exist"
                        loginProgress.visibility = View.INVISIBLE

                    }


                }

                override fun onCancelled(p0: DatabaseError) {

                    PopulateAlert(KEY_ERROR, this@LoginActivity)

                }

            })
        }





    }


}



