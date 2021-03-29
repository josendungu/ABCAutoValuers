package com.example.abcautovaluers

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.activity_schedule_view.*
import kotlinx.android.synthetic.main.activity_schedule_view.buttonSubmit
import kotlinx.android.synthetic.main.activity_valuation.*
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.android.synthetic.main.recycler_valuations.*
import kotlinx.android.synthetic.main.recycler_valuations.view.*
import java.io.File


class PhotoFragment : Fragment() {

    private lateinit var valuationInstance: ValuationInstance


    private val viewModel: ValuationViewModel by activityViewModels()
    private lateinit var valuationDetails: ValuationDetails
    private lateinit var adapter: ValuationRecyclerAdapter
    private val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val myView = inflater.inflate(R.layout.fragment_photo, container, false)

        valuationInstance = ValuationInstance(requireContext())
        adapter = viewModel.getAdapter()
        valuationDetails = viewModel.getValuationDetails()

        if (takePicIntent.resolveActivity(requireActivity().packageManager) != null) {

            myView.valuationRecyclerView.adapter = adapter

        } else {

            Toast.makeText(requireContext(), "Camera not available", Toast.LENGTH_LONG).show()

        }

        myView.buttonSubmit.setOnClickListener {

            if (valuationInstanceCheck()) {

                val intent = Intent(requireContext(), SubmittingActivity::class.java)
                intent.putExtra(SubmittingActivity.VALUATION_KEY, valuationDetails)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }

        }

        return myView
    }


    private fun handleErrorDisplay(errorMessage: String) {

        Snackbar.make(text_error, errorMessage, Snackbar.LENGTH_LONG).show()

    }

    private fun valuationInstanceCheck(): Boolean {

        val valuationData = valuationInstance.valuationPresentState

        when {
            valuationData[ValuationInstance.KEY_FRONT] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_FRONT))
                return false

            }
            valuationData[ValuationInstance.KEY_ENGINE] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_ENGINE))
                return false

            }
            valuationData[ValuationInstance.KEY_FRONT_RIGHT] == null -> {

                handleErrorDisplay(
                    getString(
                        R.string.error_message,
                        ValuationInstance.KEY_FRONT_RIGHT
                    )
                )

                return false

            }
            valuationData[ValuationInstance.KEY_FRONT_LEFT] == null -> {

                handleErrorDisplay(
                    getString(
                        R.string.error_message,
                        ValuationInstance.KEY_FRONT_LEFT
                    )
                )

                return false

            }
            valuationData[ValuationInstance.KEY_REAR] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_REAR))
                return false

            }
            valuationData[ValuationInstance.KEY_REAR_RIGHT] == null -> {

                handleErrorDisplay(
                    getString(
                        R.string.error_message,
                        ValuationInstance.KEY_REAR_RIGHT
                    )
                )

                return false

            }
            valuationData[ValuationInstance.KEY_MILLAGE] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_MILLAGE))
                return false

            }
            valuationData[ValuationInstance.KEY_HEAD_LIGHT] == null -> {

                handleErrorDisplay(
                    getString(
                        R.string.error_message,
                        ValuationInstance.KEY_HEAD_LIGHT
                    )
                )
                return false

            }
            valuationData[ValuationInstance.KEY_DASHBOARD] == null -> {

                handleErrorDisplay(
                    getString(
                        R.string.error_message,
                        ValuationInstance.KEY_DASHBOARD
                    )
                )
                return false

            }
            valuationData[ValuationInstance.KEY_RADIO] == null -> {

                handleErrorDisplay(getString(R.string.error_message, ValuationInstance.KEY_RADIO))
                return false

            }
            valuationData[ValuationInstance.KEY_INSURANCE] == null -> {

                handleErrorDisplay(
                    getString(
                        R.string.error_message,
                        ValuationInstance.KEY_INSURANCE
                    )
                )
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

}