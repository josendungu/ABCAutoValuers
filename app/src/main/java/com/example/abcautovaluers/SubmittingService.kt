package com.example.abcautovaluers

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.gson.Gson
import java.util.*

class SubmittingService : IntentService("SubmittingService") {

    private val tag = "SubmittingService"
    private lateinit var mDriveServiceHelper: DriveServiceHelper


    override fun onHandleIntent(p0: Intent?) {

        val account:GoogleSignInAccount = p0?.extras?.get("account") as GoogleSignInAccount
        val resultReceiver: ResultReceiver = p0.extras?.get("receiver") as ResultReceiver

        mDriveServiceHelper = DriveServiceHelper(getGoogleDriveService(account))

        handleSaveValuation()

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

    private fun handleSaveValuation() {

        var googleDriveFileHolder: GoogleDriveFileHolder? = null

        mDriveServiceHelper.searchFolder("Valuations")
            .addOnSuccessListener {

                googleDriveFileHolder = it
                val id = it.id


                if (it.id == null){

                    mDriveServiceHelper.createFolder("Valuations", null)
                        .addOnSuccessListener { googleDriveFileHolder ->
                            val gson = Gson()
                            Log.d(
                                tag, "onSuccess: " + gson.toJson(googleDriveFileHolder)
                            )

                            //Continue adding


                        }
                        .addOnFailureListener { e ->
                            Log.d(
                                tag, "onFailure: " + e.message
                            )

                        }

                } else {

                    mDriveServiceHelper.createFile(it.id)
                        .addOnSuccessListener {

                            Toast.makeText(this, "File was added", Toast.LENGTH_LONG).show()

                        }
                        .addOnFailureListener {

                            Toast.makeText(this, "Error adding file", Toast.LENGTH_LONG).show()

                        }

                }

            }
            .addOnFailureListener {

                Log.d(tag, "Error: ${it.toString()}")
            }

    }



}