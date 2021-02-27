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
    activity: Activity
) {

    private lateinit var view: View

    private lateinit var dialog: androidx.appcompat.app.AlertDialog
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
                    val intent = Intent(activity, ValuationActivity::class.java)
                    activity.startActivity(intent)

                }

                view.button_start.setOnClickListener {

                    ValuationInstance(activity).clearInstance()
                    dialog.cancel()
                    val intent = Intent(activity, ValuationActivity::class.java)
                    activity.startActivity(intent)

                }

                dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                dialog.show()

            }
            KEY_ERROR -> {

                view = inflater.inflate(R.layout.alert_error, null)
                alert.setView(view)
                alert.setCancelable(false)
                dialog = alert.create()

                view.button_action.setOnClickListener {

                    dialog.cancel()

                }

            }
            KEY_USER_ADD_FAILURE -> {

                view = inflater.inflate(R.layout.alert_user_add_error, null)
                alert.setView(view)
                alert.setCancelable(false)
                dialog = alert.create()

                view.button_action.setOnClickListener {

                    dialog.cancel()
                    val intent = Intent(activity, DashboardActivity::class.java)
                    intent.putExtra(DashboardActivity.USER_ADDED, false)
                    activity.startActivity(intent)

                }

            }
            KEY_ADDING_USER -> {

                view = inflater.inflate(R.layout.alert_adding_user, null)
                alert.setView(view)
                alert.setCancelable(false)
                dialog = alert.create()

            }
            KEY_BACK_PRESSED_ALERT -> {

                view = inflater.inflate(R.layout.back_pressed_alert, null)

                alert.setView(view)
                alert.setCancelable(false)
                dialog = alert.create()
                view.button_save.setOnClickListener {

                    dialog.cancel()
                    val intent = Intent(activity, DashboardActivity::class.java)
                    intent.putExtra(DashboardActivity.USER_ADDED, false)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(intent)

                }

                view.button_do_not.setOnClickListener {

                    ValuationInstance(activity).clearInstance()
                    dialog.cancel()
                    val intent = Intent(activity, DashboardActivity::class.java)
                    intent.putExtra(DashboardActivity.USER_ADDED, false)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(intent)

                }

                dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                dialog.show()

            }
            KEY_SUCCESS_IMAGES -> {

                view = inflater.inflate(R.layout.alert_success, null)

                alert.setView(view)
                alert.setCancelable(false)
                dialog = alert.create()


                view.button_action.setOnClickListener {

                    dialog.cancel()
                    val intent = Intent(activity, DashboardActivity::class.java)
                    intent.putExtra(DashboardActivity.USER_ADDED, false)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(intent)
                    ValuationInstance(activity).clearInstance()

                }

                dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                dialog.show()

            }
            KEY_ERROR_UPLOAD -> {

                view = inflater.inflate(R.layout.alert, null)

                alert.setView(view)
                alert.setCancelable(false)
                dialog = alert.create()


                view.button_action.setOnClickListener {

                    dialog.cancel()
                    val intent = Intent(activity, ValuationActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(intent)

                }

                dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                dialog.show()
            }
        }
    }

}
