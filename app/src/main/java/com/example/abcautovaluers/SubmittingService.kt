package com.example.abcautovaluers

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
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
import com.google.gson.JsonArray
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class SubmittingService : IntentService("SubmittingService") {

    private val tag = "SubmittingService"
    private lateinit var mDriveServiceHelper: DriveServiceHelper
    private lateinit var valuationData: HashMap<String, File>

    private lateinit var plateNumber: String
    private lateinit var resultReceiver: ResultReceiver
    private lateinit var picList: MutableList<File>

    override fun onHandleIntent(p0: Intent?) {

        val account:GoogleSignInAccount = p0?.extras?.get("account") as GoogleSignInAccount
        resultReceiver = p0.extras?.get("receiver") as ResultReceiver
        valuationData = p0.extras?.getSerializable("data") as HashMap<String, File>
        plateNumber = p0.extras?.get("plate_no") as String;

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

        val googleDriveFileHolder: GoogleDriveFileHolder? = null

        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
        val formattedDate = formatter.format(date)
        val specificFolder = "$plateNumber/$formattedDate"

        mDriveServiceHelper.searchFolder("Valuations")
            .addOnSuccessListener {

                val id = it.id

                if (it.id == null) {

                    mDriveServiceHelper.createFolder("Valuations", null)
                        .addOnSuccessListener { googleDriveFileHolderCreated ->
                            val gsonValuation = Gson()
                            Log.d(
                                tag, "onSuccess: " + gsonValuation.toJson(googleDriveFileHolder)
                            )

                            val bundle = Bundle()
                            bundle.putString(BUNDLE_FOLDER_CREATED, "Appropriate folders have been created")
                            resultReceiver.send(FOLDER_CREATED, bundle)

                            mDriveServiceHelper.uploadImages(
                                valuationData,
                                googleDriveFileHolderCreated.id
                            )
                                .addOnSuccessListener {

                                    val gsonLog = Gson()

                                    val bundle1 = Bundle()
                                    bundle.putString(BUNDLE_IMAGES_UPLOADED, "Images have been uploaded")
                                    resultReceiver.send(FOLDER_CREATED, bundle1)
                                    Log.d(
                                        tag, "onSuccess: " + gsonLog.toJson(googleDriveFileHolder)
                                    )

                                }
                                .addOnFailureListener { exceptionLogBook ->

                                    Log.d(tag, exceptionLogBook.toString())
                                    showFailure(exceptionLogBook)

                                }
                        }
                        .addOnFailureListener { e ->
                            Log.d(
                                tag, "onFailure: " + e.message
                            )

                        }

                } else {

                    mDriveServiceHelper.createFolder(specificFolder, it.id)
                        .addOnSuccessListener { driveFolder ->

                            val specificFolderId = driveFolder.id
                            val gson = Gson()
                            Log.d(
                                tag, "onSuccess: Specific folder: " + gson.toJson(
                                    googleDriveFileHolder
                                )
                            )

                            val bundle = Bundle()
                            bundle.putString(BUNDLE_FOLDER_CREATED, "Appropriate folders have been created")
                            resultReceiver.send(FOLDER_CREATED, bundle)

                            mDriveServiceHelper.uploadImages(
                                valuationData,
                                specificFolderId
                            )
                                .addOnSuccessListener {

                                    val gsonLog = Gson()

                                    val bundle1 = Bundle()
                                    bundle.putString(BUNDLE_FOLDER_CREATED, "Appropriate folders have been created")
                                    resultReceiver.send(FOLDER_CREATED, bundle1)
                                    Log.d(
                                        tag, "onSuccess: " + gsonLog.toJson(googleDriveFileHolder)
                                    )

                                }
                                .addOnFailureListener { exceptionLogBook ->

                                    Log.d(tag, exceptionLogBook.toString())
                                    showFailure(exceptionLogBook)

                                }

                        }.addOnFailureListener{ exception ->

                            showFailure(exception)

                        }

                }

            }
            .addOnFailureListener {

                Log.d(tag, "Error: ${it.toString()}")
            }

    }

    private fun showFailure(e: Exception) {

        Toast.makeText(
            this,
            "There has been an error please contact the administrator ",
            Toast.LENGTH_LONG
        ).show()

    }


}