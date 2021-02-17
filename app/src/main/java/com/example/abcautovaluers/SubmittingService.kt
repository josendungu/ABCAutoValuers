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
    private lateinit var username: String
    private lateinit var resultReceiver: ResultReceiver
    private lateinit var picList: MutableList<File>

    override fun onHandleIntent(p0: Intent?) {

        val account: GoogleSignInAccount = p0?.extras?.get("account") as GoogleSignInAccount
        resultReceiver = p0.extras?.get("receiver") as ResultReceiver
        username = p0.extras?.get("username") as String
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
        Log.d("Submitting Service", username)

        mDriveServiceHelper.searchFolder(username)
            .addOnSuccessListener {

                if (it.id == null) {

                    sendFailure()
                    Log.d("Submitting Service", "Didnt get the id")

                } else {

                    mDriveServiceHelper.createFolder(specificFolder, it.id)
                        .addOnSuccessListener { specificFolderCreated ->

                            val bundle = Bundle()
                            bundle.putString(
                                BUNDLE_FOLDER_CREATED,
                                "Appropriate folders have been created"
                            )
                            resultReceiver.send(FOLDER_CREATED, bundle)

                            mDriveServiceHelper.uploadImages(
                                valuationData,
                                specificFolderCreated.id
                            )
                                .addOnSuccessListener {

                                    val gsonLog = Gson()
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

                    Log.d("Submitting Service", it.id)

                }
            }
            .addOnFailureListener {
                Log.d("Submitting Service", "Search exception: ${it.toString()}")

                sendFailure()
            }

    }

    private fun sendFailure() {

        val bundleError: Bundle = Bundle()
        bundleError.putString(BUNDLE_ERROR, "There has been an error! Please try again ")
        resultReceiver.send(ERROR_OCCURRED, bundleError)

    }


}