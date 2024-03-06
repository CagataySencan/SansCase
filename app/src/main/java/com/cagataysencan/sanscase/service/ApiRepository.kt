package com.cagataysencan.sanscase.service

import com.cagataysencan.sanscase.constant.MatchStatus
import com.cagataysencan.sanscase.model.Match
import com.cagataysencan.sanscase.model.MatchResponse
import retrofit2.HttpException
import java.io.IOException

class ApiRepository(private val apiService: ApiService) {
    suspend fun getMatches(favoriteMatches : ArrayList<Match> = ArrayList()) : NetworkResult<HashMap<String, List<Match>>> {
        return try {
            val response = apiService.getMatches()
            val processedMatches = addFavoriteMatches(response.body(),favoriteMatches)
            val processedMatchesMap = processMatches(processedMatches)
            if(processedMatchesMap.isNotEmpty()) {
                NetworkResult.Success(processedMatchesMap)
            } else {
                NetworkResult.Error(Exception(Throwable()))
            }
        } catch (e: HttpException) {
            //handles exception with the request
            NetworkResult.Error(exception = e)
        } catch (e: IOException) {
            //handles no internet exception
            NetworkResult.Error(exception = e)
        }
    }

    private fun addFavoriteMatches(matchResponse: MatchResponse?, favoriteMatches : ArrayList<Match>) : MatchResponse? {
        matchResponse?.matchArrayList?.let {matchArrayList ->
            matchArrayList.filter { item1 ->
                favoriteMatches.any { item2 ->
                    item1.id == item2.id
                }
            }.forEach { it.isFavorite = true }
        }
        return matchResponse
    }

    private fun processMatches(matchResponse: MatchResponse?) : HashMap<String, List<Match>> {
        return HashMap(matchResponse?.matchArrayList?.let { matchesList ->
            matchesList
                .filter {match ->
                    match.date != null && match.score?.scoreType == MatchStatus.MATCH_ENDED.intValue && match.tournament?.name != null
                }
                .groupBy {match ->
                    match.tournament!!.name!!
                }
                .mapValues {matchesMap ->
                    matchesMap.value.sortedBy { match -> match.date!! }
                }
        }!!)
    }
}