package com.example.abcautovaluers

import androidx.lifecycle.ViewModel

class ValuationViewModel: ViewModel() {

    private var valuationDetails = ValuationDetails()
    private lateinit var adapter: ValuationRecyclerAdapter

    fun setValuationDetails(valuationDetails: ValuationDetails){
        this.valuationDetails = valuationDetails
    }

    fun getValuationDetails(): ValuationDetails{
       return valuationDetails
    }

    fun setAdapter(adapter: ValuationRecyclerAdapter){
        this.adapter = adapter
    }

    fun getAdapter():ValuationRecyclerAdapter{
        return adapter
    }

}