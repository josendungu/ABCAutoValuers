package com.example.abcautovaluers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.api.client.json.gson.GsonFactory;
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.util.*


class DashboardActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val account = GoogleSignIn.getLastSignedInAccount(this)

        buttonNew.setOnClickListener {

            startActivity(Intent(this, ValuationActivity::class.java))

        }


    }


}