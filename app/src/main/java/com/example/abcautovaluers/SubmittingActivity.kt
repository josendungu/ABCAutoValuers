package com.example.abcautovaluers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_submitting.*
import java.util.*


class SubmittingActivity : AppCompatActivity() {

    private lateinit var mDriveServiceHelper: DriveServiceHelper
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private val tag = "SubmittingActivity"
    private val SIGNIN_CODE = 1

    private val account: GoogleSignInAccount? by lazy {
        GoogleSignIn.getLastSignedInAccount(
            applicationContext
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submitting)

        initializeGoogleSignIn()

        // you can provide  folder id in case you want to save this file inside some folder.
        // if folder id is null, it will save file to the root
        mDriveServiceHelper.createFolder("testDummyss", null)
            .addOnSuccessListener { googleDriveFileHolder ->
                val gson = Gson()
                Log.d(
                    tag, "onSuccess: " + gson.toJson(googleDriveFileHolder)
                )
            }
            .addOnFailureListener { e ->
                Log.d(
                    tag,"onFailure: " + e.message
                )
            }



    }

    private fun initializeGoogleSignIn() {

        if (account == null) {
            requestUserSignIn()
        } else {


            Log.d(tag, "Account present")
            mDriveServiceHelper = DriveServiceHelper(getGoogleDriveService(account!!))

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode)  {

            SIGNIN_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {

                    Log.d(tag, "Intent in")
                    handleSignInResult(data);
                }
            }
        }
    }

    private fun handleSignInResult(resultData: Intent) {

        GoogleSignIn.getSignedInAccountFromIntent(resultData)
            .addOnSuccessListener {

                Log.d(tag, "Signed in as ${it.email}")
                textViewAccount.text = it.email
                mDriveServiceHelper = DriveServiceHelper(account?.let { it1 ->
                    getGoogleDriveService(it1)
                })

            }

            .addOnFailureListener {

                Log.d(tag, "error ${it.toString()}")

            }

    }

    private fun requestUserSignIn() {

        mGoogleSignInClient = GoogleSignIn.getClient(this, buildGoogleSignInOptions())
        startActivityForResult(mGoogleSignInClient.signInIntent, SIGNIN_CODE)

    }

    private fun getGoogleDriveService(account: GoogleSignInAccount): Drive {

        val credential = GoogleAccountCredential.usingOAuth2(
            this, Collections.singleton(DriveScopes.DRIVE_FILE)
        )
        credential.selectedAccount = account.account

        return Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            GsonFactory(),
            credential
        )
            .setApplicationName("ABC Auto Valuers")
            .build()
    }

    private fun buildGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
    }



}