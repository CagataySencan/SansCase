package com.cagataysencan.sanscase.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cagataysencan.sanscase.constant.Constants
import com.cagataysencan.sanscase.database.AppDatabase
import com.cagataysencan.sanscase.database.DatabaseRepository
import com.cagataysencan.sanscase.model.Match
import com.cagataysencan.sanscase.service.ApiRepository
import com.cagataysencan.sanscase.service.ApiService
import com.cagataysencan.sanscase.service.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val databaseRepository = DatabaseRepository(AppDatabase.getDatabase(application)!!)
    private val apiRepository = ApiRepository(ApiService())
    private var favoriteMatchList = ArrayList<Match>()
    val matchesResponse = MutableLiveData<NetworkResult<HashMap<String, List<Match>>>>()
    val favoriteMatches =  MutableLiveData<List<Match>>()

    fun getMatches() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = apiRepository.getMatches(databaseRepository.getAllMatches())
            withContext(Dispatchers.Main) {
                matchesResponse.value = result
            }
        }
    }

    fun getFavoriteMatches() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = databaseRepository.getAllMatches()
            withContext(Dispatchers.Main) {
                favoriteMatches.value = result
            }
        }
    }

    fun favoriteAndUnfavoriteMatch(match: Match)  {
        val response = (matchesResponse.value as NetworkResult.Success)
        viewModelScope.launch(Dispatchers.IO) {
            match.id?.let {id ->
                val matchFromResponse = response.matchesMap[match.tournament!!.name]?.indexOfFirst { it.id == id }
                if(match.isFavorite) {
                    response.matchesMap[match.tournament.name]?.get(matchFromResponse!!)?.isFavorite = false
                    databaseRepository.deleteMatchById(id)
                } else {
                    response.matchesMap[match.tournament.name]?.get(matchFromResponse!!)?.isFavorite = true
                    databaseRepository.insertMatch(match)
                }
                response.matchesMap = HashMap(response.matchesMap.filterValues { it.isNotEmpty() })
                favoriteMatchList = databaseRepository.getAllMatches()
            }
            withContext(Dispatchers.Main) {
                favoriteMatches.value = favoriteMatchList
                matchesResponse.value = response
            }
        }

    }

}