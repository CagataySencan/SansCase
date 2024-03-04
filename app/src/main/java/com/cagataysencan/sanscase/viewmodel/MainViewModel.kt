package com.cagataysencan.sanscase.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cagataysencan.sanscase.model.Match
import com.cagataysencan.sanscase.service.ApiRepository
import com.cagataysencan.sanscase.service.ApiService
import com.cagataysencan.sanscase.service.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val apiRepository = ApiRepository(ApiService())
    val matchesResponse = MutableLiveData<NetworkResult<Map<String, List<Match>>?>>()


    fun getMatches() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = apiRepository.getMatches()
            withContext(Dispatchers.Main) {
                matchesResponse.value = result
            }
        }
    }

}