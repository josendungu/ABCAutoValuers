package com.example.abcautovaluers

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_insurance_details.*
import kotlinx.android.synthetic.main.fragment_insurance_details.view.*
import java.util.*

class InsuranceDetailsFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var navigate: Navigate
    private val viewModel: ValuationViewModel by activityViewModels()

    private lateinit var myView: View

    private var insurer: String? = null
    private var policyNumber: String? = null
    private var expiryDate: String? = null
    private var certNumber: String? = null

    var day = 0
    private var month: Int = 0
    private var year: Int = 0

    private var myDay: Int = 0
    private var myYear: Int = 0
    private var myMonth: Int = 0

    private var finalDate: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            navigate = (activity as Navigate)
        } catch (e: Exception) {
            throw ClassCastException(activity.toString() + " must implement MyInterface")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        myView = inflater.inflate(R.layout.fragment_insurance_details, container, false)


        myView.et_expiry_date.setOnClickListener {
            pickDate()
        }

        myView.buttonNext.setOnClickListener {

            setErrorNull()
            insurer = et_insurer.editText?.text.toString()
            policyNumber = et_policy_number.editText?.text.toString()
            expiryDate = et_expiry_date.text.toString()
            certNumber = et_cert_number.editText?.text.toString()

            if (validateDate()) {

                val valuationDetails = viewModel.getValuationDetails()

                Log.d("Details", "onCreateView: ${valuationDetails.clientName}")

                valuationDetails.insurer = insurer
                valuationDetails.policyNumber = policyNumber
                valuationDetails.expiryDate = expiryDate
                valuationDetails.insuranceCertificateNumber = certNumber

                viewModel.setValuationDetails(valuationDetails)

                navigate.moveToCarDetails()
            }

        }

        return myView
    }

    private fun pickDate() {

        getDateCalender()
        val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
        datePickerDialog.datePicker.minDate = getCalender().timeInMillis
        datePickerDialog.show()

    }

    private fun getDateCalender() {
        val calendar = getCalender()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)

    }

    private fun getCalender(): Calendar{
        return Calendar.getInstance()
    }

    private fun setErrorNull() {
        myView.et_insurer.error = null
        myView.et_policy_number.error = null
        myView.et_expiry_date.error = null
        myView.et_cert_number.error = null
    }

    private fun validateDate(): Boolean {
        when {
            insurer.isNullOrEmpty() -> {
                myView.et_insurer.error = "Field can't be empty"
                return false
            }

            policyNumber.isNullOrEmpty() -> {
                myView.et_policy_number.error = "Field can't be empty"
                return false
            }

            expiryDate.isNullOrEmpty() -> {
                myView.et_expiry_date.error = "Field can't be empty"
                return false
            }

            certNumber.isNullOrEmpty() -> {
                myView.et_cert_number.error = "Field can't be empty"
                return false
            }

            else -> {
                return true
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = dayOfMonth
        myYear = year
        myMonth = month+1

        finalDate = "$myDay/$myMonth/$myYear"
        myView.et_expiry_date.text = finalDate
    }

}