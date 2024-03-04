package com.cagataysencan.sanscase.viewmodel

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date

class DetailViewModel : ViewModel() {
    fun unixTimeToString(unixTime : Long) : String {
        val date = Date(unixTime)
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
        return formatter.format(date)
    }
}