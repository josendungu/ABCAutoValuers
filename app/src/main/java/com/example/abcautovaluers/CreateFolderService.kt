package com.example.abcautovaluers

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import java.util.*

class CreateFolderService: IntentService("CreateFolderService") {

    private lateinit var mDriveServiceHelper: DriveServiceHelper

    private lateinit var username: String
    private lateinit var resultReceiver: ResultReceiver

    override fun onHandleIntent(p0: Intent?) {

        val account: GoogleSignInAccount = p0?.extras?.get("account") as GoogleSignInAccount
        resultReceiver = p0.extras?.get("receiver") as ResultReceiver
        username = p0.extras?.get("username") as String

        Log.d("Create", "Am in")

        mDriveServiceHelper = DriveServiceHelper(getGoogleDriveService(account))

        handleCreateFolder()

    }


    private fun getGoogleDriveService(account: GoogleSignInAccount): Drive {

        val credential = GoogleAccountCredential.usingOAuth2(
            this, Collections.singleton(DriveScopes.DRIVE_FILE)
        )
        credential.selectedAccount = account.account
        Log.d("DriveService", "Created")

        return Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            GsonFactory(),
            credential
        )
            .setApplicationName("ABC Auto Valuers")
            .build()
    }

    private fun handleCreateFolder() {

        mDriveServiceHelper.searchFolder("Valuations")
            .addOnSuccessListener {
                if (it.id == null) {

                    sendFailure()
                    Log.d("Submitting Service", "Didn't get the id")

                } else {

                    mDriveServiceHelper.searchFolder(username)
                        .addOnSuccessListener { folder ->

                            if (folder.id == null){

                                mDriveServiceHelper.createFolder(username, it.id)
                                    .addOnSuccessListener {

                                        val bundle = Bundle()
                                        bundle.putString(
                                            BUNDLE_FOLDER_CREATED,
                                            "Appropriate folders have been created"
                                        )
                                        resultReceiver.send(FOLDER_CREATED, bundle)
                                    }
                                    .addOnFailureListener {

                                        sendFailure()

                                    }

                            } else {

                                val bundle = Bundle()
                                bundle.putString(
                                    BUNDLE_FOLDER_EXISTS,
                                    "Folder already exists"
                                )
                                resultReceiver.send(FOLDER_EXISTS, bundle)

                            }

                        }


                        .addOnFailureListener {

                            sendFailure()

                        }

                }

            }
            .addOnFailureListener {

                sendFailure()
            }

    }

    private fun sendFailure() {

        val bundleError: Bundle = Bundle()
        bundleError.putString(BUNDLE_ERROR, "There has been an error! Please try again ")
        resultReceiver.send(ERROR_OCCURRED, bundleError)

    }
}

