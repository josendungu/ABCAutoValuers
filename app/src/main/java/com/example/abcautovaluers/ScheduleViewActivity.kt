package com.example.abcautovaluers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ScheduleViewActivity : AppCompatActivity() {

    companion object{
        const val SCHEDULE_DETAILS = "details"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_view)
    }
}