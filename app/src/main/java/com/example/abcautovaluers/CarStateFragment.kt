package com.example.abcautovaluers

import android.R.attr.data
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_car_state.view.*


class CarStateFragment : Fragment() {

    private val viewModel: ValuationViewModel by activityViewModels()

    private var headLight: String? = null
    private lateinit var selectedAntiTheft: BooleanArray
    private var antiTheftList = mutableListOf<Int>()

    private var tyres: String? = null
    private var bodyWork: String? = null
    private var mechanical: String? = null
    private var electrical: String? = null
    private var airBags: String? = null
    private var finalAntiTheft = mutableListOf<String>()
    private var extras: String? = null
    private var generalCondition: String? = null

    private lateinit var myView: View

    private lateinit var navigate: Navigate

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            navigate = (activity as Navigate)
        } catch (e: Exception) {
            throw ClassCastException(activity.toString() + " must implement MyInterface")
        }

    }

    private val lightsArray = arrayOf(
        "Type of lights",
        "Halogen",
        "LED",
        "HID",
        "Xenon",
        "Smart Xenon"
    )

    private val antiTheftArray = arrayOf(
        "Alarm",
        "Cut-out",
        "Tracker",
        "Immobilizer",
        "Central Lock",
        "Key-less entry"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myView = inflater.inflate(R.layout.fragment_car_state, container, false)

        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, lightsArray)
        myView.et_lights.adapter = adapter


        myView.buttonNext.setOnClickListener {

            setErrorNull()

            bodyWork = myView.et_body_work.text.toString()
            extras = myView.et_extras.text.toString()
            electrical = myView.et_electrical.text.toString()
            mechanical = myView.et_mechanical.text.toString()
            tyres = myView.et_tyres.text.toString()
            airBags = myView.et_airbags.text.toString()
            generalCondition = myView.et_general_condition.text.toString()

            if (validateData()) {
                if (headLight == lightsArray[0]){
                    myView.lights_error.visibility = View.VISIBLE
                    myView.lights_error.text = resources.getString(R.string.select_lights)
                } else {
                    val valuationDetails = viewModel.getValuationDetails()

                    valuationDetails.bodyWork = bodyWork
                    valuationDetails.extras = extras
                    valuationDetails.antiTheftDevice = finalAntiTheft
                    valuationDetails.electrical = electrical
                    valuationDetails.mechanical = mechanical
                    valuationDetails.tyres = tyres
                    valuationDetails.numberOfAirbags = airBags
                    valuationDetails.typesOfLights = headLight
                    valuationDetails.generalCondition = generalCondition

                    viewModel.setValuationDetails(valuationDetails)

                    navigate.moveToPhotoDetails()
                }
            }
        }

        selectedAntiTheft = BooleanArray(8)

        myView.et_anti_theft.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Select types of anti-theft devices")
            builder.setCancelable(false)
            builder.setMultiChoiceItems(
                antiTheftArray,
                selectedAntiTheft
            ) { dialog, which, isChecked ->
                if (isChecked) {
                    antiTheftList.add(which)
                    antiTheftList.sort()
                } else {
                    antiTheftList.remove(which)
                }
            }

            builder.setPositiveButton("OK") { dialogInterface, i ->
                val stringBuilder = StringBuilder()
                for (j in antiTheftList.indices) {
                    finalAntiTheft.add(antiTheftArray[antiTheftList[j]])
                    stringBuilder.append(antiTheftArray[antiTheftList[j]])

                    if (j != antiTheftList.size - 1) {
                        stringBuilder.append(", ")
                    }

                }


                myView.et_anti_theft.text = stringBuilder.toString()
            }

            builder.setNegativeButton("Cancel") { dialogInterface, i ->
                dialogInterface.dismiss()
            }

            builder.setNeutralButton("Clear all") { dialogInterface, i ->
                for (i in selectedAntiTheft.indices) {
                    selectedAntiTheft[i] = false
                    antiTheftList.clear()
                    myView.et_anti_theft.text = ""
                }
            }

            val mDialog: AlertDialog = builder.create()
            builder.show()
        }


        myView.et_lights.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    headLight = parent?.getItemAtPosition(position) as String
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        return myView
    }

    private fun setErrorNull() {
        myView.et_tyres.error = null
        myView.et_body_work.error = null
        myView.et_mechanical.error = null
        myView.et_electrical.error = null
        myView.et_airbags.error = null
        myView.et_anti_theft.error = null
        myView.lights_error.text = null
        myView.et_general_condition.error = null

        myView.lights_error.visibility = View.GONE

    }

    private fun validateData(): Boolean {
        when {
            tyres.isNullOrEmpty() -> {
                myView.et_tyres.error = "Field cannot be empty"
                return false
            }
            bodyWork.isNullOrEmpty() -> {
                myView.et_body_work.error = "Field cannot be empty"
                return false
            }
            mechanical.isNullOrEmpty() -> {
                myView.et_mechanical.error = "Field cannot be empty"
                return false
            }
            electrical.isNullOrEmpty() -> {
                myView.et_electrical.error = "Field cannot be empty"
                return false
            }
            airBags.isNullOrEmpty() -> {
                myView.et_airbags.error = "Field cannot be empty"
                return false
            }
            finalAntiTheft.isEmpty() -> {
                myView.et_anti_theft.error = "Field cannot be empty"
                return false
            }
            headLight.isNullOrEmpty() -> {
                myView.lights_error.visibility = View.VISIBLE
                myView.lights_error.text = resources.getString(R.string.empty_error)
                return false
            }
            generalCondition.isNullOrEmpty() -> {
                myView.et_general_condition.error = "Field cannot be empty"
                return false
            }
            else -> {
                return true
            }
        }
    }


}