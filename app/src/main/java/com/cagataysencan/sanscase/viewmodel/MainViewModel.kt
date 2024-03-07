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
    val matchesResponse = MutableLiveData<NetworkResult<List<List<Match>>>>()
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

    fun toggleFavorite(match: Match) : Boolean {
        if(matchesResponse.value != null && match.id != null) {
            val currentMatches = (matchesResponse.value as NetworkResult.Success)
            var favoriteMatchList = ArrayList<Match>()
            viewModelScope.launch(Dispatchers.IO) {
                val favoriteMatchId = currentMatches.matchList.flatten().find { it.id == match.id }
                match.isFavorite = !match.isFavorite
                favoriteMatchId?.let {
                    currentMatches.matchList.flatten().first { it.id == match.id }.isFavorite = match.isFavorite
                }
                if (!match.isFavorite) matchRepository.deleteMatchById(match.id) else matchRepository.insertMatch(match)
                currentMatches.matchList = currentMatches.matchList.filter { it.isNotEmpty() }
                favoriteMatchList = matchRepository.getAllMatches()
                withContext(Dispatchers.Main) {
                    favoriteMatches.value = favoriteMatchList
                    matchesResponse.value = currentMatches
                }
            }
            return true
        } else return false
    }
}