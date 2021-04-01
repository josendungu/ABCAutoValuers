package com.example.abcautovaluers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_completed_list.*
import kotlinx.android.synthetic.main.activity_completed_list.fetchProgress
import kotlinx.android.synthetic.main.activity_completed_list.statusDisplay
import kotlinx.android.synthetic.main.activity_schedule_list.*


class CompletedListActivity : AppCompatActivity() {

    private var adapter =  CompletedRecyclerAdapter(this)

    private var clickState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_list)

        setUpRecyclerView()

        statusDisplay.setOnClickListener {
            if (clickState){
                getValuationDetails()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getValuationDetails()

    }

    private fun getValuationDetails(){

        resetState()

        val reference = FirebaseUtil.openFirebaseReference("CompletedValuations")

        reference.get().addOnCompleteListener {
            fetchProgress.visibility = View.INVISIBLE
            if (it.isSuccessful){
                statusDisplay.visibility = View.INVISIBLE
                val valuationDetails = mutableListOf<ValuationDetails>()
                val result = it.result
                result?.let{
                    result.children.map { snapShot ->
                        val valuationDetail = snapShot.getValue(ValuationDetails::class.java)
                        valuationDetail?.valuationId = snapShot.key
                        if (valuationDetail != null) {
                            valuationDetails.add(valuationDetail)
                        }
                    }
                }

                adapter.setData(valuationDetails)

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
        complete_recycler.adapter = adapter
        complete_recycler.layoutManager = LinearLayoutManager(this)
    }
}