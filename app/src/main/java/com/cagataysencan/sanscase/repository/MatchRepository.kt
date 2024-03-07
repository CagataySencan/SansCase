package com.cagataysencan.sanscase.repository

import com.cagataysencan.sanscase.constant.MatchStatus
import com.cagataysencan.sanscase.database.MatchDao
import com.cagataysencan.sanscase.model.Match
import com.cagataysencan.sanscase.model.MatchResponse
import com.cagataysencan.sanscase.service.ApiService
import com.cagataysencan.sanscase.service.NetworkResult
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MatchRepository @Inject constructor(private val matchDao: MatchDao, private val apiService: ApiService) {
    suspend fun getMatches(favoriteMatches : ArrayList<Match> = ArrayList()) : NetworkResult<List<List<Match>>> {
        return try {
            val response = apiService.getMatches()
            val processedMatches = addFavoriteMatches(response.body(),favoriteMatches)
            val processedMatchesList = processMatches(processedMatches)
            if(!processedMatchesList.isNullOrEmpty()) {
                NetworkResult.Success(processedMatchesList)
            } else {
                NetworkResult.Error(Exception(Throwable()))
            }
        } catch (e: HttpException) {
            NetworkResult.Error(exception = e)
        } catch (e: IOException) {
            NetworkResult.Error(exception = e)
        }
    }

    suspend fun insertMatch(match: Match) {
        matchDao.insertMatch(match)
    }

    suspend fun getAllMatches(): ArrayList<Match> {
        return ArrayList(matchDao.getAllMatches())
    }

    suspend fun deleteMatchById(id: Long) {
        matchDao.deleteMatchById(id)
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

    private fun processMatches(matchResponse: MatchResponse?) : List<List<Match>>? {
        return matchResponse?.matchArrayList?.let { matchesList ->
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
                .values.toList()
        }
    }
}