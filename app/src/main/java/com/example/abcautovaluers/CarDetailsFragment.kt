package com.example.abcautovaluers

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_car_details.*
import kotlinx.android.synthetic.main.fragment_car_details.buttonNext
import kotlinx.android.synthetic.main.fragment_car_details.view.*
import kotlinx.android.synthetic.main.fragment_client_details.*

class CarDetailsFragment : Fragment() {

    private lateinit var navigate: Navigate
    private val viewModel: ValuationViewModel by activityViewModels()
    private lateinit var scheduleDetails: ScheduleDetails
    private lateinit var myView: View

    private var vehicleMakeModel: String? = null
    private var registrationNumber: String? = null
    private var bodyType: String? = null
    private var colour: String? = null
    private var transmission: String? = null
    private var chassis: String? = null
    private var fuelType: String? = null
    private var engineNumber: String? = null
    private var engineType: String? = null
    private var engineCapacity: String? = null
    private var odometerReading: String? = null
    private var gearBox: String? = null



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

        myView = inflater.inflate(R.layout.fragment_car_details, container, false)
        scheduleDetails = ValuationInstance(requireContext()).scheduleDetails
        myView.et_plate_number.editText?.setText(scheduleDetails.plateNumber)

        myView.buttonNext.setOnClickListener {

            setErrorNull()

            vehicleMakeModel = et_vehicle_make.editText?.text.toString()
            registrationNumber = et_plate_number.editText?.text.toString()
            bodyType = et_body_type.editText?.text.toString()
            colour = et_color.editText?.text.toString()
            transmission = et_transmission.editText?.text.toString()
            chassis = et_chassis.editText?.text.toString()
            fuelType = et_fuel_type.editText?.text.toString()
            engineNumber = et_engine_number.editText?.text.toString()
            engineType = et_engine_type.editText?.text.toString()
            engineCapacity = et_engine_capacity.editText?.text.toString()
            odometerReading = et_odometer.editText?.text.toString()
            gearBox = et_gear_box.editText?.text.toString()


            if (validateData()){
                val valuationDetails = viewModel.getValuationDetails()

                valuationDetails.vehicleMakeModel = vehicleMakeModel
                valuationDetails.registrationNumber = registrationNumber
                valuationDetails.bodyType = bodyType
                valuationDetails.color = colour
                valuationDetails.transmission = transmission
                valuationDetails.chassisNumber = chassis
                valuationDetails.fuelType = fuelType
                valuationDetails.engineNumber = engineNumber
                valuationDetails.engineCapacity = engineCapacity
                valuationDetails.engineType = engineType
                valuationDetails.odometerReading = odometerReading
                valuationDetails.gearBok = gearBox

                viewModel.setValuationDetails(valuationDetails)

                navigate.moveToCarState()
            }
        }


        return myView
    }

    private fun setErrorNull() {
        myView.et_vehicle_make.error = null
        myView.et_plate_number.error = null
        myView.et_body_type.error = null
        myView.et_color.error = null
        myView.et_transmission.error = null
        myView.et_chassis.error = null
        myView.et_fuel_type.error = null
        myView.et_engine_number.error =null
        myView.et_engine_capacity.error = null
        myView.et_engine_type.error = null
        myView.et_odometer.error = null
        myView.et_gear_box.error = null
    }

    private fun validateData(): Boolean {
        when {
            vehicleMakeModel.isNullOrEmpty() -> {
                myView.et_vehicle_make.error = "Field can't be empty"
                return false
            }
            registrationNumber.isNullOrEmpty() -> {
                myView.et_plate_number.error = "Field can't be empty"
                return false
            }
            bodyType.isNullOrEmpty() -> {
                myView.et_body_type.error = "Field can't be empty"
                return false
            }
            colour.isNullOrEmpty() -> {
                myView.et_color.error = "Field can't be empty"
                return false
            }
            transmission.isNullOrEmpty() -> {
                myView.et_transmission.error = "Field can't be empty"
                return false
            }
            chassis.isNullOrEmpty() -> {
                myView.et_chassis.error = "Field can't be empty"
                return false
            }
            fuelType.isNullOrEmpty() -> {
                myView.et_fuel_type.error = "Field can't be empty"
                return false
            }
            engineNumber.isNullOrEmpty() -> {
                myView.et_engine_number.error = "Field can't be empty"
                return false
            }
            engineCapacity.isNullOrEmpty() -> {
                myView.et_engine_capacity.error = "Field can't be empty"
                return false
            }
            engineType.isNullOrEmpty() -> {
                myView.et_engine_type.error = "Field can't be empty"
                return false
            }
            odometerReading.isNullOrEmpty() -> {
                myView.et_odometer.error = "Field can't be empty"
                return false
            }
            gearBox.isNullOrEmpty() -> {
                myView.et_gear_box.error = "Field can't be empty"
                return false
            }
            else -> {
                return true
            }

        }
    }

}