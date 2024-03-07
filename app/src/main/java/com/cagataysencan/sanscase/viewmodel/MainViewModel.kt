package com.cagataysencan.sanscase.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cagataysencan.sanscase.model.Match
import com.cagataysencan.sanscase.repository.MatchRepository
import com.cagataysencan.sanscase.service.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val matchRepository: MatchRepository) : ViewModel() {
    val matchesResponse = MutableLiveData<NetworkResult<HashMap<String, List<Match>>>>()
    val favoriteMatches = MutableLiveData<List<Match>>()

    fun getMatches() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = matchRepository.getMatches(matchRepository.getAllMatches())
            withContext(Dispatchers.Main) {
                matchesResponse.value = result
            }
        }
    }

    fun getFavoriteMatches() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = matchRepository.getAllMatches()
            withContext(Dispatchers.Main) {
                favoriteMatches.value = result
            }
        }
    }

    fun toggleFavorite(match: Match)  {
        val currentMatches = (matchesResponse.value as NetworkResult.Success)
        var favoriteMatchList = ArrayList<Match>()
        viewModelScope.launch(Dispatchers.IO) {
            match.id?.let {id ->
                val favoriteMatchId = currentMatches.matchesMap[match.tournament!!.name]?.indexOfFirst { it.id == id }
                match.isFavorite = !match.isFavorite
                favoriteMatchId?.let {
                    currentMatches.matchesMap[match.tournament.name]!![it].isFavorite = match.isFavorite
                }
                if (!match.isFavorite) matchRepository.deleteMatchById(id) else matchRepository.insertMatch(match)
                currentMatches.matchesMap = HashMap(currentMatches.matchesMap.filterValues { it.isNotEmpty() })
                favoriteMatchList = matchRepository.getAllMatches()
            }
            withContext(Dispatchers.Main) {
                favoriteMatches.value = favoriteMatchList
                matchesResponse.value = currentMatches
            }
        }
    }
}