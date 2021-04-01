package com.example.abcautovaluers

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_schedule_view.*

class ScheduleViewActivity : AppCompatActivity() {

    companion object {
        const val SCHEDULE_DETAILS = "details"
    }

    private lateinit var scheduleDetails: ScheduleDetails

    private val REQUEST_CALL = 1

    private var users = emptyList<User?>()
    private var selectedUser: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_view)

        val user = SessionManager(this).userDetails

        val valuationInstance = ValuationInstance(this)

        scheduleDetails = intent.getParcelableExtra(SCHEDULE_DETAILS)

        textDate.text = scheduleDetails.day
        textEmail.text = scheduleDetails.email
        "${scheduleDetails.surname} ${scheduleDetails.firstName} ${scheduleDetails.lastName}".also { textNames.text = it }
        "${scheduleDetails.phoneNumber}".also { textPhoneNumber.text = it }
        textPlateNumber.text = scheduleDetails.plateNumber
        textTime.text = scheduleDetails.time


        buttonCall.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:${scheduleDetails.phoneNumber}")

            if (checkCallPermission()){
                startActivity(callIntent)
            } else {
                spinnerError.visibility = View.VISIBLE
                spinnerError.text = getString(R.string.allow_permission)
            }


        }


        if (user.admin == true) {

            buttonSubmit.text = resources.getString(R.string.submit)

            setViewsVisible()
            val reference = FirebaseUtil.openFirebaseReference("Users")
            reference.get().addOnCompleteListener {
                if (it.isSuccessful) {

                    val result = it.result
                    result?.let {
                        users = result.children.map { snapShot ->
                            snapShot.getValue(User::class.java)
                        }
                    }

                    val userNames = mutableListOf<String>()
                    userNames.add("Assign user")

                    for (item in users) {
                        if (item?.admin != true){
                            item?.username?.let { it1 -> userNames.add(it1) }
                        }
                    }

                    val spinnerAdapter =
                        ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, userNames)
                    spinner.adapter = spinnerAdapter


                }
            }


            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0){
                        selectedUser = users[position - 1]
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    selectedUser = null
                }
            }
        }


        buttonSubmit.setOnClickListener {

            if (user.admin == true) {

                progressBarSubmit.visibility = View.VISIBLE
                if (selectedUser != null ) {
                    scheduleDetails.assignedTo = selectedUser?.username


                    val reference =
                        FirebaseUtil.openFirebaseReference("ScheduledValuations/${scheduleDetails.scheduleId}/assignedTo")
                    reference.setValue(selectedUser!!.username)
                        .addOnSuccessListener {
                            progressBarSubmit.visibility = View.INVISIBLE
                            val intent = Intent(this, DashboardActivity::class.java)
                            intent.putExtra(DashboardActivity.USER_ADDED, false)
                            intent.putExtra(DashboardActivity.VALUATION_DELETED, false)
                            intent.putExtra(DashboardActivity.ASSIGNED, true)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Snackbar.make(
                                snackViewCont,
                                "There was some problem assigning. Please try again",
                                Snackbar.LENGTH_LONG
                            ).show()
                            progressBarSubmit.visibility = View.INVISIBLE
                        }


                } else {
                    spinnerError.visibility = View.VISIBLE
                }
            } else {

                if (valuationInstance.checkValuation()) {

                    PopulateAlert(KEY_VALUATION_ALERT, this, scheduleDetails)

                } else {

                    valuationInstance.addScheduleDetails(scheduleDetails)
                    val intent = Intent(this, ValuationActivity::class.java)
                    startActivity(intent)

                }
            }

        }


    }

    private fun checkCallPermission() : Boolean {
        return if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CALL
            )

            ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED


        } else {
            true
        }
    }

    private fun setViewsVisible() {
        spinner.visibility = View.VISIBLE
        spinnerError.visibility = View.INVISIBLE
    }


}