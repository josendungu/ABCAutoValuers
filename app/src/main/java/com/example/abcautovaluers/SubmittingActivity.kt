package com.example.abcautovaluers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import kotlinx.android.synthetic.main.activity_submitting.*
import java.io.File
import java.io.FileWriter
import java.io.IOException


class SubmittingActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private val tag = "Submitting"
    private val SIGNIN_CODE = 1
    private lateinit var mContext: Context
    private lateinit var scheduleDetails: ScheduleDetails
    private lateinit var valuationInstance: ValuationInstance

    private lateinit var valuationDetails: ValuationDetails

    private lateinit var file: File

    companion object{
        const val VALUATION_KEY = "valuation_key"
    }

    private val handler = Handler()

    private var account: GoogleSignInAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submitting)

        valuationInstance = ValuationInstance(this)
        scheduleDetails =  valuationInstance.scheduleDetails

        valuationDetails = intent.getParcelableExtra(VALUATION_KEY)
        Log.d(tag, "Valuation details: " + valuationDetails.toString())
        mContext = this

        textError.setOnClickListener{
            it.visibility = View.GONE
            initializeImagesUpload()
        }

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, {

            if (it) {

                connection_state.visibility = View.VISIBLE
                connection_state.text = getString(R.string.connected)
                connection_state.setBackgroundColor(resources.getColor(R.color.colorSuccess))

                Handler().postDelayed({

                    connection_state.visibility = View.INVISIBLE

                }, 10000)

            } else {

                connection_state.visibility = View.VISIBLE
                connection_state.text = getString(R.string.not_connected)
                connection_state.setBackgroundColor(resources.getColor(R.color.colorPrimary))

            }
        })

        account = GoogleSignIn.getLastSignedInAccount(this)

        initializeGoogleSignIn()


    }



    private fun initializeImagesUpload(){

        val resultReceiver = MyResultReceiver(handler)
        val valuationData = valuationInstance.valuationData

        val storageDirectory = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

        try {
            val file = File.createTempFile("details", ".txt", storageDirectory)
            val fw = FileWriter(file.absoluteFile, false)
            fw.write(valuationDetails.toString())
            fw.close()
            this.file = file
        } catch (ioe: IOException) {
            textError.visibility = View.VISIBLE
            System.err.println("IOException: " + ioe.message)
            return
        }


        val intent = Intent(this, SubmittingService::class.java)
        intent.putExtra("account", account)
        intent.putExtra("folderId", SessionManager(this).folderId)
        intent.putExtra("valuationFile", file)
        intent.putExtra("data", valuationData)
        intent.putExtra("plate_no", valuationInstance.plateNumber)
        intent.putExtra("receiver", resultReceiver)
        startService(intent)

    }



    private fun initializeGoogleSignIn() {

        if (account?.email == null) {

            requestUserSignIn()

        } else {

            accountSelected.text = account?.email
            Log.d(tag, "Account present")
            initializeImagesUpload()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            SIGNIN_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {

                    Log.d(tag, "Intent in")
                    handleSignInResult(data)
                }
            }
        }
    }

    private fun handleSignInResult(resultData: Intent) {

        GoogleSignIn.getSignedInAccountFromIntent(resultData)
            .addOnSuccessListener {

                Log.d(tag, "Signed in as ${it.email}")
                accountSelected.text = it.email
                account = it
                initializeImagesUpload()

            }

            .addOnFailureListener {

                Log.d(tag, "error ${it.toString()}")

            }

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

    private inner class MyResultReceiver(handler: Handler?) : ResultReceiver(handler) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            super.onReceiveResult(resultCode, resultData)

            when(resultCode){

                FOLDER_CREATED -> {

                    Toast.makeText(mContext, "Appropriate folders created.", Toast.LENGTH_LONG)
                        .show()

                }

                IMAGES_UPLOADED -> {
                    Toast.makeText(mContext, "Photos successfully uploaded.", Toast.LENGTH_LONG)
                        .show()

                }

                FILE_UPLOADED -> {

                    val deleteReference = FirebaseUtil.openFirebaseReference("ScheduledValuations")
                    val addReference = FirebaseUtil.openFirebaseReference("CompletedValuations")

                    addReference.push().setValue(valuationDetails)
                        .addOnSuccessListener {
                            deleteReference.child(scheduleDetails.scheduleId!!).removeValue()
                                .addOnSuccessListener {
                                    PopulateAlert(KEY_SUCCESS_IMAGES, mContext as Activity)
                                }
                        }
                        .addOnFailureListener {
                            PopulateAlert(KEY_ERROR_UPLOAD, mContext as Activity)
                        }

                }

                FILE_FAILURE -> {

                    PopulateAlert(KEY_ERROR_UPLOAD, mContext as Activity)

                }
                ERROR_OCCURRED -> {

                    PopulateAlert(KEY_ERROR_UPLOAD, mContext as Activity)
                    Log.d("Error:", resultData?.get(BUNDLE_ERROR) as String)

                }

            }



        }
    }


}