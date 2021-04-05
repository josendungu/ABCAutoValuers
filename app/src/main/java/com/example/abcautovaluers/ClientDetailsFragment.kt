package com.example.abcautovaluers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_client_details.*
import kotlinx.android.synthetic.main.fragment_client_details.view.*


class ClientDetailsFragment : Fragment() {

    private lateinit var scheduleDetails: ScheduleDetails
    private var name: String? = null
    private var email: String? = null
    private var phoneNumber: String? = null
    private var address: String? = null

    private lateinit var myView: View

    private lateinit var navigate: Navigate

    private val viewModel: ValuationViewModel by activityViewModels()

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
    ): View {

        myView = inflater.inflate(R.layout.fragment_client_details, container, false)

        val userSession = SessionManager(requireContext())
        val valuationInstance = ValuationInstance(requireContext())

        scheduleDetails = valuationInstance.scheduleDetails


        myView.et_name.editText?.setText("${scheduleDetails.surname} ${scheduleDetails.firstName} ${scheduleDetails.lastName}")
        myView.et_email.editText?.setText(scheduleDetails.email)
        myView.et_phone.editText?.setText(scheduleDetails.phoneNumber)

        myView.buttonNext.setOnClickListener {

            setErrorNull()
            name = et_name.editText?.text.toString()
            address = et_address.editText?.text.toString()
            email = et_email.editText?.text.toString()
            phoneNumber = et_phone.editText?.text.toString()

            if (validateData()) {
                val valuationDetails = ValuationDetails(
                    clientName = name,
                    address = address,
                    email = email,
                    phone = phoneNumber,
                    valuatedBy = userSession.username,
                    idProvided = myView.identification.isChecked,
                    valuationLetterProvided = myView.valuation_letter.isChecked,
                    pinCertificateProvided = myView.kra.isChecked,
                    logBookProvided = myView.log_book.isChecked,
                    instructions = scheduleDetails.instructions
                )

                viewModel.setValuationDetails(valuationDetails)
                navigate.moveToInsurance()

            }
        }

        return myView
    }

    private fun setErrorNull() {
        myView.et_name.error = null
        myView.et_address.error = null
        myView.et_email.error = null
        myView.et_phone.error = null
    }

    private fun validateData(): Boolean {
        when {
            name.isNullOrEmpty() -> {
                myView.et_name.error = "Field can't be empty"
                return false
            }
            address.isNullOrEmpty() -> {
                myView.et_address.error = "Field can't be empty"
                return false
            }
            email.isNullOrEmpty() -> {
                myView.et_email.error = "Field can't be empty"
                return false
            }
            phoneNumber.isNullOrEmpty() -> {
                myView.et_phone.error = "Field can't be empty"
                return false
            }
            else -> {
                return true
            }
        }
    }


}