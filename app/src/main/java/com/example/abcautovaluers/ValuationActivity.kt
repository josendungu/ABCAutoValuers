package com.example.abcautovaluers

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
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
import kotlinx.android.synthetic.main.activity_valuation.*
import java.io.File

class ValuationActivity : AppCompatActivity() {

    private val tag = "Valuation"
    private lateinit var photoFile: File
    private lateinit var valuationInstance: ValuationInstance
    private val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    private var resultPresent: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valuation)

        valuationInstance = ValuationInstance(this)

        if (valuationInstance.checkValuation()){

            handleValuationPresent()

        }


        if (takePicIntent.resolveActivity(this.packageManager) != null) {

            add_log_book.setOnClickListener { initializeCamIntent(LOG_BOOK_CODE, LOG_BOOK_NAME) }
            add_kra.setOnClickListener { initializeCamIntent(KRA_CODE, KRA_NAME) }
            add_id.setOnClickListener { initializeCamIntent(ID_CODE, ID_NAME) }
            add_instructions.setOnClickListener {
                initializeCamIntent(
                    INSTRUCTIONS_CODE,
                    INSTRUCTIONS_NAME
                )
            }
            add_front.setOnClickListener { initializeCamIntent(FRONT_CODE, FRONT_NAME) }
            add_front_right.setOnClickListener {
                initializeCamIntent(
                    FRONT_RIGHT_CODE,
                    FRONT_RIGHT_NAME
                )
            }
            add_front_left.setOnClickListener {
                initializeCamIntent(
                    FRONT_LEFT_CODE,
                    FRONT_LEFT_NAME
                )
            }
            add_rear.setOnClickListener { initializeCamIntent(REAR_CODE, REAR_NAME) }
            add_rear_right.setOnClickListener {
                initializeCamIntent(
                    REAR_RIGHT_CODE,
                    REAR_RIGHT_NAME
                )
            }
            add_rear_left.setOnClickListener { initializeCamIntent(REAR_LEFT_CODE, REAR_LEFT_NAME) }
            add_millage.setOnClickListener { initializeCamIntent(MILLAGE_CODE, MILLAGE_NAME) }
            add_head_light.setOnClickListener {
                initializeCamIntent(
                    HEAD_LIGHT_CODE,
                    HEAD_LIGHT_NAME
                )
            }
            add_dashboard.setOnClickListener { initializeCamIntent(DASHBOARD_CODE, DASHBOARD_NAME) }
            add_radio.setOnClickListener { initializeCamIntent(RADIO_CODE, RADIO_NAME) }
            add_insurance.setOnClickListener { initializeCamIntent(INSURANCE_CODE, INSURANCE_NAME) }
            add_chassis.setOnClickListener { initializeCamIntent(CHASSIS_CODE, CHASSIS_NAME) }

        } else {

            Toast.makeText(this, "Camera not available", Toast.LENGTH_LONG).show()

        }

        buttonSubmit.setOnClickListener {

            val plateNumber = textPlateNumber.editText?.text.toString()

            if (plateNumber.isNotEmpty()){

                if (valuationInstanceCheck()){

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

    private fun handleErrorDisplay(state: Int = 0){

        if (state == 1){

            text_error.visibility = View.VISIBLE

        } else {

            text_error.visibility = View.INVISIBLE

        }

    }


    private fun handleValuationPresent() {

        val valuationData = valuationInstance.valuationPresentState

        if (valuationData[ValuationInstance.KEY_PLATE_NO] != null){

            textPlateNumber.editText?.setText(valuationData[ValuationInstance.KEY_PLATE_NO])

        }

        if (valuationData[ValuationInstance.KEY_LOG_BOOK] != null){

            updateAddedItem(pic_log, add_log_book, ic_log)

        }

        if (valuationData[ValuationInstance.KEY_KRA] != null){

            updateAddedItem(pic_kra, add_kra, ic_kra)

        }

        if (valuationData[ValuationInstance.KEY_ID] != null) {

            updateAddedItem(pic_ID, add_id, ic_id)

        }
        if (valuationData[ValuationInstance.KEY_INSTRUCTIONS] != null) {

            updateAddedItem(pic_instructions, add_instructions, ic_instructions)

        }
        if (valuationData[ValuationInstance.KEY_FRONT] != null) {

            updateAddedItem(pic_front, add_front, ic_front)

        }
        if (valuationData[ValuationInstance.KEY_FRONT_RIGHT] != null) {

            updateAddedItem(pic_front_right, add_front_right, ic_front_right)

        }
        if (valuationData[ValuationInstance.KEY_FRONT_LEFT] != null) {

            updateAddedItem(pic_front_left, add_front_left, ic_front_left)

        }
        if (valuationData[ValuationInstance.KEY_REAR] != null) {

            updateAddedItem(pic_rear, add_rear, ic_rear)

        }

        if (valuationData[ValuationInstance.KEY_REAR_LEFT] != null) {

            updateAddedItem(pic_rear_left, add_rear_left, ic_rear_left)

        }
        if (valuationData[ValuationInstance.KEY_REAR_RIGHT] != null) {

            updateAddedItem(pic_rear_right, add_rear_right, ic_rear_right)

        }
        if(valuationData[ValuationInstance.KEY_MILLAGE] != null) {

            updateAddedItem(pic_millage, add_millage, ic_millage)

        }
        if (valuationData[ValuationInstance.KEY_HEAD_LIGHT] != null) {

            updateAddedItem(pic_head_light, add_head_light, ic_head_light)

        }
        if(valuationData[ValuationInstance.KEY_DASHBOARD] != null) {

            updateAddedItem(pic_dashboard, add_dashboard, ic_dashboard)

        }
        if (valuationData[ValuationInstance.KEY_RADIO] != null) {

            updateAddedItem(pic_radio, add_radio, ic_radio)

        }
        if (valuationData[ValuationInstance.KEY_INSURANCE] != null) {

            updateAddedItem(pic_insurance, add_insurance, ic_insurance)

        }
        if(valuationData[ValuationInstance.KEY_CHASSIS] != null) {

            updateAddedItem(pic_chassis, add_chassis, ic_chassis)

        }

    }

    private fun valuationInstanceCheck(): Boolean {

        val valuationData = valuationInstance.valuationPresentState

        when {
            valuationData[ValuationInstance.KEY_LOG_BOOK] == null -> {

                handleErrorDisplay(1)
                text_error.text = getString(R.string.error_message, ValuationInstance.KEY_LOG_BOOK)
                return false

            }
            valuationData[ValuationInstance.KEY_KRA] == null -> {

                handleErrorDisplay(1)
                text_error.text = getString(R.string.error_message, ValuationInstance.KEY_KRA)
                return false

            }
            valuationData[ValuationInstance.KEY_ID] == null -> {

                handleErrorDisplay(1)
                text_error.text = getString(R.string.error_message, ValuationInstance.KEY_ID)
                return false

            }
            valuationData[ValuationInstance.KEY_INSTRUCTIONS] == null -> {

                handleErrorDisplay(1)
                text_error.text = getString(R.string.error_message, ValuationInstance.KEY_INSTRUCTIONS)
                return false

            }
            valuationData[ValuationInstance.KEY_FRONT] == null -> {

                handleErrorDisplay(1)
                text_error.text = getString(R.string.error_message, ValuationInstance.KEY_FRONT)
                return false

            }
            valuationData[ValuationInstance.KEY_FRONT_RIGHT] == null -> {

                handleErrorDisplay(1)
                text_error.text = getString(R.string.error_message, ValuationInstance.KEY_FRONT_RIGHT)
                return false

            }
            valuationData[ValuationInstance.KEY_FRONT_LEFT] == null -> {

                handleErrorDisplay(1)
                text_error.text = getString(R.string.error_message, ValuationInstance.KEY_FRONT_LEFT)
                return false

            }
            valuationData[ValuationInstance.KEY_REAR] == null -> {

                handleErrorDisplay(1)
                text_error.text = getString(R.string.error_message, ValuationInstance.KEY_REAR)
                return false

            }
            valuationData[ValuationInstance.KEY_REAR_RIGHT] == null -> {

                handleErrorDisplay(1)
                text_error.text = getString(R.string.error_message, ValuationInstance.KEY_REAR_RIGHT)
                return false

            }
            valuationData[ValuationInstance.KEY_MILLAGE] == null -> {

                handleErrorDisplay(1)
                text_error.text = getString(R.string.error_message, ValuationInstance.KEY_MILLAGE)
                return false

            }
            valuationData[ValuationInstance.KEY_HEAD_LIGHT] == null -> {

                handleErrorDisplay(1)
                text_error.text = getString(R.string.error_message, ValuationInstance.KEY_HEAD_LIGHT)
                return false

            }
            valuationData[ValuationInstance.KEY_DASHBOARD] == null -> {

                handleErrorDisplay(1)
                text_error.text = getString(R.string.error_message, ValuationInstance.KEY_DASHBOARD)
                return false

            }
            valuationData[ValuationInstance.KEY_RADIO] == null -> {

                handleErrorDisplay(1)
                text_error.text = getString(R.string.error_message, ValuationInstance.KEY_RADIO)
                return false

            }
            valuationData[ValuationInstance.KEY_INSURANCE] == null -> {

                handleErrorDisplay(1)
                text_error.text = getString(R.string.error_message, ValuationInstance.KEY_INSURANCE)
                return false

            }
            valuationData[ValuationInstance.KEY_CHASSIS] == null -> {

                handleErrorDisplay(1)
                text_error.text = getString(R.string.error_message, ValuationInstance.KEY_CHASSIS)
                return false

            }
            else -> {

                return true

            }
        }


    }

    private fun updateAddedItem(relLayoutOutline: View, relLayoutBackground: View, imageView: ImageView){

        relLayoutOutline.background = ContextCompat.getDrawable(this, R.drawable.pic_item_background_added)
        relLayoutBackground.background = ContextCompat.getDrawable(this, R.drawable.circular_background_added)
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_added))

    }

    private fun initializeCamIntent(code: Int, fileName: String) {

        photoFile = getPhotoFile(fileName)
        val fileProvider = FileProvider.getUriForFile(
            this,
            "com.example.abcautovaluers.fileprovider",
            photoFile
        )

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val resolvedIntentActivities: List<ResolveInfo> = this.packageManager
            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolvedIntentInfo in resolvedIntentActivities) {
            val packageName: String = resolvedIntentInfo.activityInfo.packageName
            this.grantUriPermission(
                packageName,
                fileProvider,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        Log.d(tag, fileProvider.toString())

        takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        startActivityForResult(takePicIntent, code)

    }

    private fun getPhotoFile(fileName: String): File {

        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)

    }

    private fun handleResultPresentProperty(){

        resultPresent = true

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {

            LOG_BOOK_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    valuationInstance.addValuationItem(
                        ValuationInstance.KEY_LOG_BOOK,
                        photoFile.absolutePath
                    )
                    updateAddedItem(pic_log, add_log_book, ic_log)
                    handleResultPresentProperty()
                    Log.d(tag, "Intent in: ${photoFile.absolutePath}")

                }
            }

            KRA_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    valuationInstance.addValuationItem(
                        ValuationInstance.KEY_KRA,
                        photoFile.absolutePath
                    )
                    updateAddedItem(pic_kra, add_kra, ic_kra)
                    handleResultPresentProperty()
                    Log.d(tag, "Intent in: ${photoFile.absolutePath}")

                }
            }

            ID_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    valuationInstance.addValuationItem(
                        ValuationInstance.KEY_ID,
                        photoFile.absolutePath
                    )
                    updateAddedItem(pic_ID, add_id, ic_id)
                    handleResultPresentProperty()
                    Log.d(tag, "Intent in: ${photoFile.absolutePath}")

                }
            }
            INSTRUCTIONS_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    valuationInstance.addValuationItem(
                        ValuationInstance.KEY_INSTRUCTIONS,
                        photoFile.absolutePath
                    )
                    updateAddedItem(pic_instructions, add_instructions, ic_instructions)
                    handleResultPresentProperty()
                    Log.d(tag, "Intent in: ${photoFile.absolutePath}")

                }
            }
            FRONT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    valuationInstance.addValuationItem(
                        ValuationInstance.KEY_FRONT,
                        photoFile.absolutePath
                    )
                    updateAddedItem(pic_front, add_front, ic_front)
                    Log.d(tag, "Intent in: ${photoFile.absolutePath}")
                    handleResultPresentProperty()
                    //val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

                }
            }
            FRONT_RIGHT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    valuationInstance.addValuationItem(
                        ValuationInstance.KEY_FRONT_RIGHT,
                        photoFile.absolutePath
                    )
                    updateAddedItem(pic_front_right, add_front_right, ic_front)
                    handleResultPresentProperty()
                    Log.d(tag, "Intent in: ${photoFile.absolutePath}")
                    //val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

                }
            }
            FRONT_LEFT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    valuationInstance.addValuationItem(
                        ValuationInstance.KEY_FRONT_LEFT,
                        photoFile.absolutePath
                    )
                    updateAddedItem(pic_front_left, add_front_left, ic_front_left)
                    handleResultPresentProperty()
                    Log.d(tag, "Intent in: ${photoFile.absolutePath}")
                    //val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

                }
            }
            REAR_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    valuationInstance.addValuationItem(
                        ValuationInstance.KEY_REAR,
                        photoFile.absolutePath
                    )
                    updateAddedItem(pic_rear, add_rear, ic_rear)
                    handleResultPresentProperty()
                    Log.d(tag, "Intent in: ${photoFile.absolutePath}")
                    //val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

                }
            }
            REAR_RIGHT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    valuationInstance.addValuationItem(
                        ValuationInstance.KEY_REAR_RIGHT,
                        photoFile.absolutePath
                    )
                    updateAddedItem(pic_rear_right, add_rear_right, ic_rear_right)
                    handleResultPresentProperty()
                    Log.d(tag, "Intent in: ${photoFile.absolutePath}")
                    //val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

                }
            }
            REAR_LEFT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    valuationInstance.addValuationItem(
                        ValuationInstance.KEY_REAR_LEFT,
                        photoFile.absolutePath
                    )
                    updateAddedItem(pic_rear_left, add_rear_left, ic_rear_left)
                    handleResultPresentProperty()
                    Log.d(tag, "Intent in: ${photoFile.absolutePath}")
                    //val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

                }
            }
            MILLAGE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    valuationInstance.addValuationItem(
                        ValuationInstance.KEY_MILLAGE,
                        photoFile.absolutePath
                    )
                    updateAddedItem(pic_millage, add_millage, ic_millage)
                    handleResultPresentProperty()
                    Log.d(tag, "Intent in: ${photoFile.absolutePath}")
                    //val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

                }
            }
            HEAD_LIGHT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    valuationInstance.addValuationItem(
                        ValuationInstance.KEY_HEAD_LIGHT,
                        photoFile.absolutePath
                    )
                    updateAddedItem(pic_head_light, add_head_light, ic_head_light)
                    handleResultPresentProperty()
                    Log.d(tag, "Intent in: ${photoFile.absolutePath}")
                    //val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

                }
            }
            DASHBOARD_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    valuationInstance.addValuationItem(
                        ValuationInstance.KEY_DASHBOARD,
                        photoFile.absolutePath
                    )
                    updateAddedItem(pic_dashboard, add_dashboard, ic_dashboard)
                    handleResultPresentProperty()
                    Log.d(tag, "Intent in: ${photoFile.absolutePath}")
                    //val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

                }
            }
            RADIO_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    valuationInstance.addValuationItem(
                        ValuationInstance.KEY_RADIO,
                        photoFile.absolutePath
                    )
                    updateAddedItem(pic_radio, add_radio, ic_radio)
                    handleResultPresentProperty()
                    Log.d(tag, "Intent in: ${photoFile.absolutePath}")
                    //val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

                }
            }
            INSURANCE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    valuationInstance.addValuationItem(
                        ValuationInstance.KEY_INSURANCE,
                        photoFile.absolutePath
                    )
                    updateAddedItem(pic_insurance, add_insurance, ic_insurance)
                    handleResultPresentProperty()
                    Log.d(tag, "Intent in: ${photoFile.absolutePath}")
                    //val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

                }
            }
            CHASSIS_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    valuationInstance.addValuationItem(
                        ValuationInstance.KEY_CHASSIS,
                        photoFile.absolutePath
                    )
                    updateAddedItem(pic_chassis, add_chassis, ic_chassis)
                    handleResultPresentProperty()
                    Log.d(tag, "Intent in: ${photoFile.absolutePath}")
                    //val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

                }
            }
            else -> {

                super.onActivityResult(requestCode, resultCode, data)

            }

        }

    }

    override fun onBackPressed() {

        PopulateAlert(KEY_BACK_PRESSED_ALERT, this)

    }

}