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
    val loading = MutableLiveData<Boolean>()

    // Gets favorite matches from the database and the matches from the server.
    fun getMatches() {
        loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val matches = matchRepository.getMatches()
            val favoriteMatchesFromDB = matchRepository.getMatchesFromDB()
            withContext(Dispatchers.Main) {
                matchesResponse.value = matches
                favoriteMatches.value = favoriteMatchesFromDB
                loading.value = false
            }
        }
    }

    // Handles the favorite click event of a match.
    fun toggleFavorite(match: Match)  {
        var favoriteMatchList: ArrayList<Match>
        // If matches exist, toggle favorite status of the match.
        if(matchesResponse.value != null && match.id != null) {
            val currentMatches = (matchesResponse.value as NetworkResult.Success)
            // Change the status of the match.
            match.isFavorite = !match.isFavorite

            // Check if the match still exists in server response. If exist update it's isFavorite property
            currentMatches.matchList.flatten().find { it.id == match.id }?.let {
                it.isFavorite = match.isFavorite
            }

            // If the match isFavorite, delete the match from the database. If not, add it to the database. And Update the livedata
            viewModelScope.launch(Dispatchers.IO) {
                if (!match.isFavorite) matchRepository.deleteMatchById(match.id) else matchRepository.insertMatch(match)
                favoriteMatchList = matchRepository.getMatchesFromDB()
                withContext(Dispatchers.Main) {
                    favoriteMatches.value = favoriteMatchList
                    matchesResponse.value = currentMatches
                }
            }
        // If matches no longer exist, remove the match from the database.
        } else if (matchesResponse.value == null && match.id != null) {
            viewModelScope.launch(Dispatchers.IO) {
                matchRepository.deleteMatchById(match.id)
                favoriteMatchList = matchRepository.getMatchesFromDB()
                withContext(Dispatchers.Main) {
                    favoriteMatches.value = favoriteMatchList
                }
            }
        }
    }
}