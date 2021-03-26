package com.example.abcautovaluers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

private var scheduleList: List<ScheduleDetails?> = emptyList()

class ScheduleAdapter(private val context: Context): RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val editTextName = itemView.findViewById<TextView>(R.id.names)
        private val editTextLocation = itemView.findViewById<TextView>(R.id.location)
        private val editTextPlateNumber = itemView.findViewById<TextView>(R.id.plateNumber)

        fun bind(scheduleDetails: ScheduleDetails?){

            if (scheduleDetails != null){
                val name = "${scheduleDetails.surname} ${scheduleDetails.firstName}"
                val location = "${scheduleDetails.county} ${scheduleDetails.town}"
                editTextName.text = name
                editTextLocation.text = location
                editTextPlateNumber.text = scheduleDetails.plateNumber

                itemView.setOnClickListener {
                    val intent = Intent(context, ScheduleViewActivity::class.java)
                    intent.putExtra(ScheduleViewActivity.SCHEDULE_DETAILS, scheduleDetails)
                    context.startActivity(intent)
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(scheduleList[position])
    }

    override fun getItemCount(): Int = scheduleList.size

    fun setData(list: List<ScheduleDetails?>){
        scheduleList = list
        notifyDataSetChanged()
    }
}