package com.cagataysencan.sanscase.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel() {
    fun unixTimeToString(unixTime : Long) : String {
        val date = Date(unixTime)
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
        return formatter.format(date)
    }
}