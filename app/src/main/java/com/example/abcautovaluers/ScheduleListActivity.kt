package com.example.abcautovaluers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_schedule_list.*
import kotlinx.android.synthetic.main.activity_schedule_view.*

class ScheduleListActivity : AppCompatActivity() {

    private val adapter = ScheduleAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_list)

        setUpRecyclerView()

        val reference = FirebaseUtil.openFirebaseReference("ScheduledValuations")

        reference.get().addOnCompleteListener {
            if (it.isSuccessful){

                var scheduleDetailsList = emptyList<ScheduleDetails?>()
                val result = it.result
                result?.let{
                    scheduleDetailsList = result.children.map { snapShot ->
                        snapShot.getValue(ScheduleDetails::class.java)
                    }
                }

                adapter.setData(scheduleDetailsList)
            }
        }

    }

    private fun setUpRecyclerView() {
        schedule_recycler.adapter = adapter
        schedule_recycler.layoutManager = LinearLayoutManager(this)
    }
}