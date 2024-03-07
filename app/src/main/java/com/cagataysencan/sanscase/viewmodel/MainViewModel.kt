package com.cagataysencan.sanscase.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cagataysencan.sanscase.database.DatabaseRepository
import com.cagataysencan.sanscase.model.Match
import com.cagataysencan.sanscase.service.ApiRepository
import com.cagataysencan.sanscase.service.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val databaseRepository: DatabaseRepository, private val apiRepository: ApiRepository) : ViewModel() {
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

    fun toggleFavorite(match: Match)  {
        val currentMatches = (matchesResponse.value as NetworkResult.Success)
        viewModelScope.launch(Dispatchers.IO) {
            match.id?.let {id ->
                val favoriteMatchId = currentMatches.matchesMap[match.tournament!!.name]?.indexOfFirst { it.id == id }
                match.isFavorite = !match.isFavorite
                favoriteMatchId?.let {
                    currentMatches.matchesMap[match.tournament.name]!![it].isFavorite = match.isFavorite
                }
                if (!match.isFavorite) databaseRepository.deleteMatchById(id) else databaseRepository.insertMatch(match)
                currentMatches.matchesMap = HashMap(currentMatches.matchesMap.filterValues { it.isNotEmpty() })
                favoriteMatchList = databaseRepository.getAllMatches()
            }
            withContext(Dispatchers.Main) {
                favoriteMatches.value = favoriteMatchList
                matchesResponse.value = currentMatches
            }
        }
    }
}