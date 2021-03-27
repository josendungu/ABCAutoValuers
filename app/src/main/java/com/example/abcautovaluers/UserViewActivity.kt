package com.example.abcautovaluers

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_submitting.*
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

    private val handler = Handler()

    private val SIGNIN_CODE = 1

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var account: GoogleSignInAccount? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_view)

        buttonSubmit.setOnClickListener {

            et_username.error = null
            et_email.error = null
            et_password.error = null

            userProgress.visibility = View.VISIBLE

            mUsername = et_username.editText?.text.toString().trim()
            mEmail = et_email.editText?.text.toString().trim()
            mPassword =  et_password.editText?.text.toString().trim()
            mIsAdmin = checkBox.isChecked

            if (validateUser() && validatePassword() && validateEmail()){

                account = GoogleSignIn.getLastSignedInAccount(this)

                validateUsername()

            } else {

                Toast.makeText(this, "Some error", Toast.LENGTH_LONG).show()
                hideProgress()

            }


        }

    }

    private fun hideProgress(){

        userProgress.visibility = View.INVISIBLE

    }

    private fun initializeGoogleSignIn() {

        if (account?.email == null) {

            requestUserSignIn()

        } else {

            Log.d("Account", "Account present")
            initializeCreateFolder()

        }

    }

    private fun initializeCreateFolder() {

        val resultReceiver = MyResultReceiver(handler)
        val intent = Intent(this, CreateFolderService::class.java)
        intent.putExtra("account", account)
        intent.putExtra("receiver", resultReceiver)
        intent.putExtra("username", mUsername)
        startService(intent)

    }

    private fun requestUserSignIn() {

        mGoogleSignInClient = GoogleSignIn.getClient(this, buildGoogleSignInOptions())
        startActivityForResult(mGoogleSignInClient.signInIntent, SIGNIN_CODE)

    }

    private fun buildGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(DriveScopes.DRIVE))
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .requestEmail()
            .build()
    }

    private fun validateUsername() {

        val reference = FirebaseUtil.openFirebaseReference("Users").orderByChild("username").equalTo(mUsername)

        reference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                if (!p0.exists()){

                    initializeGoogleSignIn()

                } else {

                    et_username.error = "Username is already taken. Enter another one and try again"
                    hideProgress()

                }

            }

            override fun onCancelled(p0: DatabaseError) {

                PopulateAlert(KEY_USER_ADD_FAILURE, this@UserViewActivity)

            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            SIGNIN_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {

                    handleSignInResult(data)
                }
            }
        }
    }

    private fun handleSignInResult(resultData: Intent) {

        GoogleSignIn.getSignedInAccountFromIntent(resultData)
            .addOnSuccessListener {

                Log.d("Account Added", "Signed in as ${it.email}")
                accountSelected.text = it.email
                account = it
                initializeCreateFolder()

            }

            .addOnFailureListener {

                PopulateAlert(KEY_ERROR,this)
                hideProgress()
                Log.d("Account not added", "error ${it.toString()}")

            }

    }

    private fun addUser(folderId: String) {

        val user = User(mUsername, mPassword, mEmail, mIsAdmin, folderId)
        val addReference = FirebaseUtil.openFirebaseReference("Users/$mUsername")
        addReference.setValue(user)

            .addOnSuccessListener {

                Log.d("User Add","added")
                val intent = Intent(this@UserViewActivity, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra(DashboardActivity.USER_ADDED, true)
                intent.putExtra(DashboardActivity.ASSIGNED, false)
                startActivity(intent)

            }
            .addOnFailureListener {

                Log.d("Add User Exception", it.toString())
                hideProgress()
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

    private inner class MyResultReceiver(handler: Handler?) : ResultReceiver(handler) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            super.onReceiveResult(resultCode, resultData)


            when(resultCode){

                FOLDER_CREATED -> {

                    val folderId = resultData?.get(BUNDLE_FOLDER_ID)
                    addUser(folderId as String)
                    Toast.makeText(this@UserViewActivity, "Folder was successfully created.", Toast.LENGTH_LONG).show()

                }
                FOLDER_EXISTS -> {

                    val folderId = resultData?.get(BUNDLE_FOLDER_ID)
                    addUser(folderId as String)
                    Toast.makeText(this@UserViewActivity, "Folder already exists", Toast.LENGTH_LONG).show()

                }
                ERROR_OCCURRED -> {

                    hideProgress()
                    PopulateAlert(KEY_ERROR, this@UserViewActivity)


                }
            }
        }
    }

}