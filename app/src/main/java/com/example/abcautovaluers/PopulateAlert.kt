package com.example.abcautovaluers

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog.Builder
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.alert.view.*
import kotlinx.android.synthetic.main.alert.view.tv_info
import kotlinx.android.synthetic.main.back_pressed_alert.view.*
import kotlinx.android.synthetic.main.valuation_alert.view.*

class PopulateAlert(
    instance: Int,
    activity: Activity,
    message: String? = null,
    private val location: Int? = null
) {

    private lateinit var view: View

    private var dialog: androidx.appcompat.app.AlertDialog
    private val inflater by lazy { LayoutInflater.from(activity) }

    init {

        val alert = Builder(activity, android.R.style.Theme_Material_Dialog_Alert)

        when (instance) {
            KEY_VALUATION_ALERT -> {

                view = inflater.inflate(R.layout.valuation_alert, null)

                alert.setView(view)
                alert.setCancelable(false)
                dialog = alert.create()
                view.button_proceed.setOnClickListener {

                    dialog.cancel()

                }

                view.button_start.setOnClickListener {

                    ValuationInstance(activity).clearInstance()
                    dialog.cancel()
                    val intent = Intent(activity, ValuationActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(intent)

                }

                dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                dialog.show()

            }
            KEY_BACK_PRESSED_ALERT -> {

                view = inflater.inflate(R.layout.back_pressed_alert, null)

                alert.setView(view)
                alert.setCancelable(false)
                dialog = alert.create()
                view.button_save.setOnClickListener {

                    dialog.cancel()
                    val intent = Intent(activity, DashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(intent)

                }

                view.button_do_not.setOnClickListener {

                    ValuationInstance(activity).clearInstance()
                    dialog.cancel()
                    val intent = Intent(activity, DashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(intent)

                }

                dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                dialog.show()

            }
            else -> {

                view = inflater.inflate(R.layout.alert, null)
                view.tv_info.text = message


                if (location == null) {

                    view.button_action.visibility = View.INVISIBLE
                    alert.setCancelable(true)

                } else {

                    view.button_action.setOnClickListener {

                        //val location = getLocation()

    //                if (location != null) {
    //
    //                    val intent = Intent(activity, location)
    //                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
    //                    activity.startActivity(intent)
    //
    //                }

                    }

                }

                alert.setView(view)

                dialog = alert.create()
                dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                dialog.show()

            }
        }
    }

//    private fun getLocation(): Class<*>? {
//
//        return when (location) {
//            LOGIN_ACTIVITY_REF -> LoginActivity::class.java
//            DASHBOARD_REF -> DashboardActivity::class.java
//            else -> null
//        }
//
//
//    }
}
