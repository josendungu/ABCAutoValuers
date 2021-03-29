package com.example.abcautovaluers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_schedule_list.*
import kotlinx.android.synthetic.main.activity_schedule_view.*

class ScheduleListActivity : AppCompatActivity() {

    private val adapter = ScheduleAdapter(this)
    private var clickState = false

    private lateinit var userSession: SessionManager

    override fun onResume() {
        super.onResume()

        adapter.setData(listOf<ScheduleDetails>())

        getScheduledList()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_list)

        setUpRecyclerView()
        userSession = SessionManager(this)

        statusDisplay.setOnClickListener {
            if (clickState){
                getScheduledList()
            }
        }


    }

    private fun getScheduledList(){

        resetState()

        val reference = FirebaseUtil.openFirebaseReference("ScheduledValuations")

        reference.get().addOnCompleteListener {
            fetchProgress.visibility = View.INVISIBLE
            if (it.isSuccessful){
                statusDisplay.visibility = View.INVISIBLE
                val scheduleDetailsList = mutableListOf<ScheduleDetails>()
                val result = it.result
                result?.let{
                    result.children.map { snapShot ->
                        val scheduleDetail = snapShot.getValue(ScheduleDetails::class.java)
                        scheduleDetail?.scheduleId = snapShot.key
                        if (scheduleDetail != null) {

                            if (userSession.userDetails.admin == true){
                                if (scheduleDetail.assignedTo == null){
                                    scheduleDetailsList.add(scheduleDetail)
                                }
                            } else {
                                if (scheduleDetail.assignedTo == userSession.username){
                                    scheduleDetailsList.add(scheduleDetail)
                                }
                            }


                        }
                    }
                }

                adapter.setData(scheduleDetailsList)

            } else {
                clickState = true
                statusDisplay.text = resources.getString(R.string.fetch_failed)
            }
        }

    }

    private fun resetState() {
        fetchProgress.visibility = View.VISIBLE
        clickState = false
        statusDisplay.text = resources.getString(R.string.fetching_valuations)

    }

    private fun setUpRecyclerView() {
        schedule_recycler.adapter = adapter
        schedule_recycler.layoutManager = LinearLayoutManager(this)
    }
}