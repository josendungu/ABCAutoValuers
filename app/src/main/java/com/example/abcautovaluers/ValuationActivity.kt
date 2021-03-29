package com.example.abcautovaluers


import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.viewModels

import androidx.fragment.app.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_valuation.*
import kotlinx.android.synthetic.main.recycler_valuations.*
import java.io.File

class ValuationActivity : AppCompatActivity(), Navigate {

    private lateinit var valuationInstance: ValuationInstance
    private var scheduleDetails: ScheduleDetails? = null

    private lateinit var adapter : ValuationRecyclerAdapter

    companion object {
        const val SCHEDULED_STRING = "schedule_data"
        const val SCHEDULE_PRESENT = "present"
    }

    private val viewModel: ValuationViewModel by viewModels()

    private lateinit var valuationDetails: ValuationDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valuation)

        adapter = ValuationRecyclerAdapter(this)

        viewModel.setAdapter(adapter)

        valuationInstance = ValuationInstance(this)

        if (valuationInstance.checkValuation()) {
            scheduleDetails = valuationInstance.scheduleDetails
        } else {

            val state = intent.getBooleanExtra(SCHEDULE_PRESENT, false)
            if (state){
                scheduleDetails = intent.getParcelableExtra(SCHEDULED_STRING)
                valuationInstance.addScheduleDetails(scheduleDetails)
            }

        }


    }

    private fun handleIfNotPresent(file: File?, key: String?) {

        if (file?.path != null) {

            val bitmap = BitmapFactory.decodeFile(file.path)

            if (bitmap == null) {

                valuationInstance.addValuationItem(key, null)

            } else {
                adapter.notifyDataSetChanged()
            }

        }

    }

    private fun getKey(requestCode: Int): String? {

        when (requestCode) {
            CHASSIS_CODE -> {

                return ValuationInstance.KEY_CHASSIS

            }
            DASHBOARD_CODE -> {

                return ValuationInstance.KEY_DASHBOARD

            }
            ENGINE_CODE -> {

                return ValuationInstance.KEY_ENGINE

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        valuationRecyclerView.adapter?.notifyDataSetChanged()
        when (requestCode) {

            LOG_BOOK_CODE -> {
                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
            }

            KRA_CODE -> {
                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
            }
            ENGINE_CODE -> {

                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
                if (resultCode == Activity.RESULT_OK) {
                    adapter.notifyDataSetChanged()
                }
            }

            ID_CODE -> {

                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
                if (resultCode == Activity.RESULT_OK) {
                    adapter.notifyDataSetChanged()
                }
            }
            INSTRUCTIONS_CODE -> {
                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
                if (resultCode == Activity.RESULT_OK) {
                    adapter.notifyDataSetChanged()
                }
            }
            FRONT_CODE -> {
                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
                if (resultCode == Activity.RESULT_OK) {
                    adapter.notifyDataSetChanged()
                }
            }
            FRONT_RIGHT_CODE -> {
                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
                if (resultCode == Activity.RESULT_OK) {
                    adapter.notifyDataSetChanged()
                }
            }
            FRONT_LEFT_CODE -> {
                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
                if (resultCode == Activity.RESULT_OK) {
                    adapter.notifyDataSetChanged()
                }
            }
            REAR_CODE -> {
                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
                if (resultCode == Activity.RESULT_OK) {
                    adapter.notifyDataSetChanged()
                }
            }
            REAR_RIGHT_CODE -> {
                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
                if (resultCode == Activity.RESULT_OK) {
                    adapter.notifyDataSetChanged()
                }
            }
            REAR_LEFT_CODE -> {
                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
                if (resultCode == Activity.RESULT_OK) {
                    adapter.notifyDataSetChanged()
                }
            }
            MILLAGE_CODE -> {
                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
                if (resultCode == Activity.RESULT_OK) {
                    adapter.notifyDataSetChanged()
                }
            }
            HEAD_LIGHT_CODE -> {
                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
                if (resultCode == Activity.RESULT_OK) {
                    adapter.notifyDataSetChanged()
                }
            }
            DASHBOARD_CODE -> {
                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
                if (resultCode == Activity.RESULT_OK) {
                    adapter.notifyDataSetChanged()
                }
            }
            RADIO_CODE -> {
                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
                if (resultCode == Activity.RESULT_OK) {
                    adapter.notifyDataSetChanged()
                }
            }
            INSURANCE_CODE -> {
                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
                if (resultCode == Activity.RESULT_OK) {
                    adapter.notifyDataSetChanged()
                }
            }
            CHASSIS_CODE -> {
                val key = getKey(requestCode)
                val file = valuationInstance.getValuationItem(key)
                handleIfNotPresent(file, key)
                if (resultCode == Activity.RESULT_OK) {
                    adapter.notifyDataSetChanged()
                }
            }
            else -> {

                super.onActivityResult(requestCode, resultCode, data)

            }

        }

    }

    override fun moveToCarDetails() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CarDetailsFragment()).commit()
    }

    override fun moveToPhotoDetails() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, PhotoFragment()).commit()
    }

    override fun moveToCarState() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CarStateFragment()).commit()
    }

    override fun moveToInsurance() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, InsuranceDetailsFragment()).commit()
    }

}