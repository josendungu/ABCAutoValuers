package com.example.abcautovaluers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import kotlinx.android.synthetic.main.activity_submitting.*


class SubmittingActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private val tag = "Submitting"
    private val SIGNIN_CODE = 1

    private lateinit var account: GoogleSignInAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submitting)

        account = GoogleSignIn.getLastSignedInAccount(this)!!

        initializeGoogleSignIn()

        textViewAccount.setOnClickListener {

            val resultReceiver = MyResultReceiver(null)
            val valuationInstance = ValuationInstance(this)
            val valuationData = valuationInstance.valuationData


            val intent = Intent(this, SubmittingService::class.java)
            intent.putExtra("account", account)
            intent.putExtra("data", valuationData)
            intent.putExtra("plate_no", valuationInstance.plateNumber)
            intent.putExtra("receiver", resultReceiver)
            startService(intent)
            //handlePrepareSaveValuation()

        }

    }



    private fun initializeGoogleSignIn() {

        if (account == null) {
            requestUserSignIn()
        } else {

            textViewAccount.text = account!!.email
            Log.d(tag, "Account present")

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
                textViewAccount.text = it.email
                account = it

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

    private class MyResultReceiver(handler: Handler?) : ResultReceiver(handler) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            super.onReceiveResult(resultCode, resultData)


        }
    }


}