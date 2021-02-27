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
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class SubmittingService : IntentService("SubmittingService") {

    private val tag = "SubmittingService"

    private lateinit var mDriveServiceHelper: DriveServiceHelper
    private lateinit var valuationData: HashMap<String, File>

    private lateinit var plateNumber: String
    private lateinit var folderId: String
    private lateinit var resultReceiver: ResultReceiver

    override fun onHandleIntent(p0: Intent?) {

        val account: GoogleSignInAccount = p0?.extras?.get("account") as GoogleSignInAccount
        resultReceiver = p0.extras?.get("receiver") as ResultReceiver
        folderId = p0.extras?.get("folderId") as String
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
        Log.d("Submitting Folder", folderId)

        mDriveServiceHelper.createFolder(specificFolder, folderId)

            .addOnSuccessListener { specificFolderCreated ->

                val bundle = Bundle()
                bundle.putString(
                    BUNDLE_FOLDER_CREATED,
                    "Appropriate folders have been created"
                )
                resultReceiver.send(FOLDER_CREATED, bundle)

                Log.d("Submitting Service", "Folder created")

                mDriveServiceHelper.uploadImages(
                    valuationData,
                    specificFolderCreated.id
                )
                    .addOnSuccessListener {

                        val bundle1 = Bundle()
                        bundle1.putString(
                            BUNDLE_IMAGES_UPLOADED,
                            "Images have been uploaded"
                        )
                        resultReceiver.send(IMAGES_UPLOADED, bundle1)

                    }
                    .addOnFailureListener { exceptionImages ->

                        val bundleErrorImages = Bundle()
                        bundleErrorImages.putString(
                            BUNDLE_FOLDER_CREATED,
                            "Error: ${exceptionImages.toString()}"
                        )
                        resultReceiver.send(ERROR_OCCURRED, bundleErrorImages)

                    }
            }
            .addOnFailureListener {
                Log.d("Submitting Service", "folder not created")
                sendFailure()
            }

    }


    private fun sendFailure() {

        val bundleError: Bundle = Bundle()
        bundleError.putString(BUNDLE_ERROR, "There has been an error! Please try again ")
        resultReceiver.send(ERROR_OCCURRED, bundleError)

    }


}