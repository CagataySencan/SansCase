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
            val result = matchRepository.getMatches()
            withContext(Dispatchers.Main) {
                matchesResponse.value = result
            }
        }
    }

    fun getFavoriteMatches() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = matchRepository.getMatchesFromDB()
            withContext(Dispatchers.Main) {
                favoriteMatches.value = result
            }
        }
    }

    fun toggleFavorite(match: Match) : Boolean {
        val isMatchFavorable = matchesResponse.value != null && match.id != null

        if(isMatchFavorable) {
            val currentMatches = (matchesResponse.value as NetworkResult.Success)
            val favoriteMatchId = currentMatches.matchList.flatten().find { it.id == match.id }
            var favoriteMatchList: ArrayList<Match>
            match.isFavorite = !match.isFavorite

            favoriteMatchId?.let {
                currentMatches.matchList.flatten().first { it.id == match.id }.isFavorite = match.isFavorite
            }

            viewModelScope.launch(Dispatchers.IO) {
                if (!match.isFavorite) matchRepository.deleteMatchById(match.id!!) else matchRepository.insertMatch(match)
                favoriteMatchList = matchRepository.getMatchesFromDB()
                withContext(Dispatchers.Main) {
                    favoriteMatches.value = favoriteMatchList
                    matchesResponse.value = currentMatches
                }
            }
        }
        return isMatchFavorable
    }
}