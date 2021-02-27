package com.example.abcautovaluers

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_valuation.*
import kotlinx.android.synthetic.main.recycler_valuations.*
import java.io.File

class ValuationActivity : AppCompatActivity() {

    private val tag = "Valuation"
    private lateinit var valuationInstance: ValuationInstance
    private val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    private var resultPresent: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valuation)

        valuationInstance = ValuationInstance(this)

        if (takePicIntent.resolveActivity(this.packageManager) != null) {

            valuationRecyclerView.adapter =
                ValuationRecyclerAdapter(this)

        } else {

            Toast.makeText(this, "Camera not available", Toast.LENGTH_LONG).show()

        }

        buttonSubmit.setOnClickListener {

            val plateNumber = textPlateNumber.editText?.text.toString()

            if (plateNumber.isNotEmpty()) {

                if (valuationInstanceCheck()) {

                    valuationInstance.addValuationItem(ValuationInstance.KEY_PLATE_NO, plateNumber)
                    val intent = Intent(this, SubmittingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                }

            } else {

                textPlateNumber.requestFocus()
                textPlateNumber.error = "Plate number can not be empty"

            }

        }

    }

    private fun handleErrorDisplay(errorMessage: String) {

        Snackbar.make(text_error, errorMessage, Snackbar.LENGTH_LONG).show()

    }


    private fun valuationInstanceCheck(): Boolean {

        val valuationData = valuationInstance.valuationPresentState

        when {
            valuationData[ValuationInstance.KEY_LOG_BOOK] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_LOG_BOOK))
                return false

            }
            valuationData[ValuationInstance.KEY_KRA] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_KRA))
                return false

            }
            valuationData[ValuationInstance.KEY_ID] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_ID))
                return false

            }
            valuationData[ValuationInstance.KEY_INSTRUCTIONS] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_INSTRUCTIONS))
                return false

            }
            valuationData[ValuationInstance.KEY_FRONT] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_FRONT))
                return false

            }
            valuationData[ValuationInstance.KEY_FRONT_RIGHT] == null -> {

                handleErrorDisplay( getString(R.string.error_message, ValuationInstance.KEY_FRONT_RIGHT))

                return false

            }
            valuationData[ValuationInstance.KEY_FRONT_LEFT] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_FRONT_LEFT))

                return false

            }
            valuationData[ValuationInstance.KEY_REAR] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_REAR))
                return false

            }
            valuationData[ValuationInstance.KEY_REAR_RIGHT] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_REAR_RIGHT))

                return false

            }
            valuationData[ValuationInstance.KEY_MILLAGE] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_MILLAGE))
                return false

            }
            valuationData[ValuationInstance.KEY_HEAD_LIGHT] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_HEAD_LIGHT))
                return false

            }
            valuationData[ValuationInstance.KEY_DASHBOARD] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_DASHBOARD))
                return false

            }
            valuationData[ValuationInstance.KEY_RADIO] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_RADIO))
                return false

            }
            valuationData[ValuationInstance.KEY_INSURANCE] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_INSURANCE))
                return false

            }
            valuationData[ValuationInstance.KEY_CHASSIS] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_CHASSIS))
                return false

            }
            else -> {

                return true

            }
        }


    }


    private fun handleResultPresentProperty() {

        resultPresent = true

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        valuationRecyclerView.adapter?.notifyDataSetChanged()
        when (requestCode) {

            LOG_BOOK_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val key = getKey(requestCode)
                    val file = valuationInstance.getValuationItem(key)
                    handleIfNotPresent(file, key)
                    handleResultPresentProperty()

                }
            }

            KRA_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val key = getKey(requestCode)
                    val file = valuationInstance.getValuationItem(key)
                    handleIfNotPresent(file, key)
                    handleResultPresentProperty()

                }
            }

            ID_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val key = getKey(requestCode)
                    val file = valuationInstance.getValuationItem(key)
                    handleIfNotPresent(file, key)
                    handleResultPresentProperty()


                }
            }
            INSTRUCTIONS_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val key = getKey(requestCode)
                    val file = valuationInstance.getValuationItem(key)
                    handleIfNotPresent(file, key)
                    handleResultPresentProperty()

                }
            }
            FRONT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val key = getKey(requestCode)
                    val file = valuationInstance.getValuationItem(key)
                    handleIfNotPresent(file, key)
                    handleResultPresentProperty()

                }
            }
            FRONT_RIGHT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val key = getKey(requestCode)
                    val file = valuationInstance.getValuationItem(key)
                    handleIfNotPresent(file, key)
                    handleResultPresentProperty()

                }
            }
            FRONT_LEFT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val key = getKey(requestCode)
                    val file = valuationInstance.getValuationItem(key)
                    handleIfNotPresent(file, key)
                    handleResultPresentProperty()

                }
            }
            REAR_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val key = getKey(requestCode)
                    val file = valuationInstance.getValuationItem(key)
                    handleIfNotPresent(file, key)
                    handleResultPresentProperty()

                }
            }
            REAR_RIGHT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val key = getKey(requestCode)
                    val file = valuationInstance.getValuationItem(key)
                    handleIfNotPresent(file, key)
                    handleResultPresentProperty()

                }
            }
            REAR_LEFT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val key = getKey(requestCode)
                    val file = valuationInstance.getValuationItem(key)
                    handleIfNotPresent(file, key)
                    handleResultPresentProperty()

                }
            }
            MILLAGE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val key = getKey(requestCode)
                    val file = valuationInstance.getValuationItem(key)
                    handleIfNotPresent(file, key)
                    handleResultPresentProperty()

                }
            }
            HEAD_LIGHT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val key = getKey(requestCode)
                    val file = valuationInstance.getValuationItem(key)
                    handleIfNotPresent(file, key)
                    handleResultPresentProperty()

                }
            }
            DASHBOARD_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val key = getKey(requestCode)
                    val file = valuationInstance.getValuationItem(key)
                    handleIfNotPresent(file, key)
                    handleResultPresentProperty()

                }
            }
            RADIO_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val key = getKey(requestCode)
                    val file = valuationInstance.getValuationItem(key)
                    handleIfNotPresent(file, key)
                    handleResultPresentProperty()

                }
            }
            INSURANCE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val key = getKey(requestCode)
                    val file = valuationInstance.getValuationItem(key)
                    handleIfNotPresent(file, key)
                    handleResultPresentProperty()

                }
            }
            CHASSIS_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val key = getKey(requestCode)
                    val file = valuationInstance.getValuationItem(key)
                    handleIfNotPresent(file, key)
                    handleResultPresentProperty()

                }
            }
            else -> {

                super.onActivityResult(requestCode, resultCode, data)

            }

        }

    }

    private fun handleIfNotPresent(file: File?, key: String?) {

        if (file?.path != null) {

            val bitmap = BitmapFactory.decodeFile(file.path)

            if (bitmap == null){

                valuationInstance.addValuationItem(key, null)

            }

        }

        //TODO: Notify data set changed

    }

    private fun getKey(requestCode: Int): String? {

        when (requestCode) {
            CHASSIS_CODE -> {

                return ValuationInstance.KEY_CHASSIS

            }
            DASHBOARD_CODE -> {

                return ValuationInstance.KEY_DASHBOARD

            }
            FRONT_CODE -> {

                return ValuationInstance.KEY_FRONT

            }
            FRONT_LEFT_CODE -> {

                return ValuationInstance.KEY_FRONT_LEFT

            }
            FRONT_RIGHT_CODE -> {

                return ValuationInstance.KEY_FRONT_RIGHT

            }
            HEAD_LIGHT_CODE -> {

                return ValuationInstance.KEY_HEAD_LIGHT

            }
            ID_CODE -> {

                return ValuationInstance.KEY_ID

            }
            REAR_CODE -> {

                return ValuationInstance.KEY_REAR

            }
            REAR_LEFT_CODE -> {

                return ValuationInstance.KEY_REAR_LEFT

            }
            REAR_RIGHT_CODE -> {

                return ValuationInstance.KEY_REAR_RIGHT

            }
            INSTRUCTIONS_CODE -> {

                return ValuationInstance.KEY_INSTRUCTIONS

            }
            INSURANCE_CODE -> {

                return ValuationInstance.KEY_INSURANCE

            }
            KRA_CODE -> {

                return ValuationInstance.KEY_KRA

            }
            LOG_BOOK_CODE -> {

                return ValuationInstance.KEY_LOG_BOOK

            }
            MILLAGE_CODE -> {

                return ValuationInstance.KEY_MILLAGE

            }
            RADIO_CODE -> {

                return ValuationInstance.KEY_RADIO

            }
            else -> {

                return null
            }

        }

    }

    override fun onBackPressed() {

        PopulateAlert(KEY_BACK_PRESSED_ALERT, this)

    }


}