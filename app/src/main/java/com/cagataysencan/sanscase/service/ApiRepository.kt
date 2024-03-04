package com.cagataysencan.sanscase.service

import com.cagataysencan.sanscase.R
import com.cagataysencan.sanscase.constant.MatchStatus
import com.cagataysencan.sanscase.model.Match
import com.cagataysencan.sanscase.model.MatchResponse
import retrofit2.HttpException
import java.io.IOException

class ApiRepository(private val apiService: ApiService) {
    suspend fun getMatches() : NetworkResult<Map<String, List<Match>>?> {
        return try {
            val response = apiService.getMatches()
            val processedMatches = processMatches(response.body())
            if(!processedMatches.isNullOrEmpty()) {
                NetworkResult.Success(processedMatches)
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

    private fun processMatches(matchResponse: MatchResponse?) : Map<String, List<Match>>? {
        return matchResponse?.data?.let { matchesList ->
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
        }
    }
}