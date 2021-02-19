package com.example.abcautovaluers

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

                addUser()

            } else {

                Toast.makeText(this, "Some error", Toast.LENGTH_LONG).show()

            }



        }

    }

    private fun addUser() {

        val user = User(mUsername, mPassword, mEmail)

        val reference = FirebaseUtil.openFirebaseReference("Users").orderByChild("username").equalTo(mUsername)

        reference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                if (!p0.exists()){

                    val addReference = FirebaseUtil.openFirebaseReference("Users")

                    addReference.setValue(user)
                        .addOnFailureListener {

                            PopulateAlert(KEY_USER_ADD_FAILURE, this@UserViewActivity)

                        }
                        .addOnSuccessListener {

                            PopulateAlert(KEY_USER_ADDED, this@UserViewActivity)

                        }

                } else {

                    PopulateAlert(KEY_USER_ADD_FAILURE, this@UserViewActivity)

                }

            }

            override fun onCancelled(p0: DatabaseError) {

                PopulateAlert(KEY_USER_ADD_FAILURE, this@UserViewActivity)

            }

        })

    }

    private fun validateEmail(): Boolean {

        val validate = Validate(mEmail, et_email)
        val state = !validate.stringEmpty() && validate.doesNotContainsSpecialCharacter()
        Log.d("New User Email", state.toString())
        return state

    }

    private fun validateUser(): Boolean {

        val validate = Validate(mUsername, et_username)
        val state = !validate.stringEmpty() && validate.doesNotContainsSpecialCharacter() && validate.fourCharacters() && validate.noWhiteSpaces()
        Log.d("New User User", state.toString())
        return state

    }

    private fun validatePassword(): Boolean{

        val validate = Validate(mPassword, et_password)
        val state = !validate.stringEmpty() && validate.noWhiteSpaces() && validate.containsDigit()
        Log.d("New User Password", state.toString())
        return state

    }

}