package com.example.abcautovaluers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

private var valuationDetails = emptyList<ValuationDetails>()

class CompletedRecyclerAdapter(val context: Context): RecyclerView.Adapter<CompletedRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val editTextName = itemView.findViewById<TextView>(R.id.names)
        private val editTextLocation = itemView.findViewById<TextView>(R.id.location)
        private val editTextPlateNumber = itemView.findViewById<TextView>(R.id.plateNumber)

        fun bind(valuationDetails: ValuationDetails?){

            if (valuationDetails != null){
                val name = valuationDetails.clientName
                val location = valuationDetails.valuatedBy
                editTextName.text = name
                editTextLocation.text = location
                editTextPlateNumber.text = valuationDetails.registrationNumber

                itemView.setOnClickListener {
                    PopulateAlert(KEY_DELETE_COMPLETED_VALUATION, context as Activity, valuationId = valuationDetails.valuationId)
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int = valuationDetails.size

    fun setData(list: List<ValuationDetails>){
        valuationDetails = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(valuationDetails[position])
    }


}