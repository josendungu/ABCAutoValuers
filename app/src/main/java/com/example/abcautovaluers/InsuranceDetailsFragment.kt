package com.example.abcautovaluers

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_car_details.*
import kotlinx.android.synthetic.main.fragment_client_details.*
import kotlinx.android.synthetic.main.fragment_client_details.buttonNext
import kotlinx.android.synthetic.main.fragment_insurance_details.*
import kotlinx.android.synthetic.main.fragment_insurance_details.view.*

class InsuranceDetailsFragment : Fragment() {

    private lateinit var navigate: Navigate
    private val viewModel: ValuationViewModel by activityViewModels()

    private lateinit var myView: View

    private var insurer: String? = null
    private var policyNumber: String? = null
    private var expiryDate: String? = null
    private var certNumber: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            navigate = (activity as Navigate)
        } catch (e: Exception){
            throw ClassCastException(activity.toString() + " must implement MyInterface")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        myView = inflater.inflate(R.layout.fragment_insurance_details, container, false)

        myView.buttonNext.setOnClickListener {

            setErrorNull()
            insurer = et_insurer.editText?.text.toString()
            policyNumber = et_policy_number.editText?.text.toString()
            expiryDate = et_expiry_date.editText?.text.toString()
            certNumber = et_cert_number.editText?.text.toString()

            if (validateDate()){

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

    private fun setErrorNull() {
        myView.et_insurer.error =null
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

}