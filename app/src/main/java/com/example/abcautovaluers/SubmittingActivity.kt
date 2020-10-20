package com.example.abcautovaluers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_submitting.*
import kotlinx.android.synthetic.main.activity_submitting.connection_state


class SubmittingActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private val tag = "Submitting"
    private val SIGNIN_CODE = 1
    private lateinit var mContext: Context

    private val handler = Handler()

    private var account: GoogleSignInAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submitting)

        mContext = this

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer {

            if (it){

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
        val valuationInstance = ValuationInstance(this)
        val valuationData = valuationInstance.valuationData


        val intent = Intent(this, SubmittingService::class.java)
        intent.putExtra("account", account)
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
                    handleSignInResult(data);
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

                    Toast.makeText(mContext, "Appropriate folders created.", Toast.LENGTH_LONG).show()

                }

                IMAGES_UPLOADED -> {

                    PopulateAlert(KEY_SUCCESS_IMAGES, mContext as Activity)

                }

                ERROR_OCCURRED -> {

                    PopulateAlert(KEY_ERROR_UPLOAD, mContext as Activity)
                }

            }



        }
    }


}