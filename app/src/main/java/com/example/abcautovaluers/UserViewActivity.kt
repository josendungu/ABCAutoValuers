package com.example.abcautovaluers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_user_view.*
import kotlinx.android.synthetic.main.activity_user_view.buttonSubmit
import kotlinx.android.synthetic.main.activity_user_view.checkBox
import kotlinx.android.synthetic.main.activity_user_view.et_password
import kotlinx.android.synthetic.main.activity_user_view.et_username

class UserViewActivity : AppCompatActivity() {

    private lateinit var mUsername: String
    private lateinit var mEmail: String
    private lateinit var mPassword: String
    private var mIsAdmin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_view)

        buttonSubmit.setOnClickListener {

            et_username.error = null
            et_email.error = null
            et_password.error = null

            mUsername = et_username.editText?.text.toString().trim()
            mEmail = et_email.editText?.text.toString().trim()
            mPassword =  et_password.editText?.text.toString().trim()
            mIsAdmin = checkBox.isChecked

            if (validateUser() && validatePassword() && validateEmail()){

                validateUsername()

            } else {

                Toast.makeText(this, "Some error", Toast.LENGTH_LONG).show()

            }



        }

    }

    private fun validateUsername() {

        val reference = FirebaseUtil.openFirebaseReference("Users").orderByChild("username").equalTo(mUsername)

        reference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                if (!p0.exists()){

                    addUser()


                } else {

                    et_username.error = "Username is already taken. Enter another one and try again"

                }

            }

            override fun onCancelled(p0: DatabaseError) {

                PopulateAlert(KEY_USER_ADD_FAILURE, this@UserViewActivity)

            }

        })

    }

    private fun addUser() {

        val user = User(mUsername, mPassword, mEmail)
        val addReference = FirebaseUtil.openFirebaseReference("Users/$mUsername")
        PopulateAlert(KEY_ADDING_USER, this@UserViewActivity)
        addReference.setValue(user)

            .addOnSuccessListener {

                Log.d("User Add","added")
                val intent = Intent(this@UserViewActivity, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra(DashboardActivity.USER_ADDED, true)
                startActivity(intent)

            }
            .addOnFailureListener {

                Log.d("Add User Exception", it.toString())
                PopulateAlert(KEY_USER_ADD_FAILURE, this@UserViewActivity)

            }
    }

    private fun validateEmail(): Boolean {

        val validate = Validate(mEmail, et_email)
        val state = !validate.stringEmpty() && validate.validateEmail()
        Log.d("New User Email", state.toString())
        return state

    }

    private fun validateUser(): Boolean {

        val validate = Validate(mUsername, et_username)
        val state = !validate.stringEmpty() && validate.doesNotContainsSpecialCharacter() && validate.fourCharacters()
        Log.d("New User User", state.toString())
        return state

    }

    private fun validatePassword(): Boolean{

        val validate = Validate(mPassword, et_password)
        val state = !validate.stringEmpty() && validate.containsDigit()
        Log.d("New User Password", state.toString())
        return state

    }

}