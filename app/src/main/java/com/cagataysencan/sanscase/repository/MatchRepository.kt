package com.cagataysencan.sanscase.repository

import com.cagataysencan.sanscase.constant.MatchStatus
import com.cagataysencan.sanscase.database.MatchDao
import com.cagataysencan.sanscase.model.Match
import com.cagataysencan.sanscase.model.MatchResponse
import com.cagataysencan.sanscase.service.ApiService
import com.cagataysencan.sanscase.service.NetworkResult
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
// Repository class for the Match model. Does all the database and api call operations for the Match model.
class MatchRepository @Inject constructor(private val matchDao: MatchDao, private val apiService: ApiService) {
    suspend fun getMatches() : NetworkResult<List<List<Match>>> {
        return try {
            val response = getMatchesFromServer()
            val processedMatches = addFavoriteMatches(response.body(), getMatchesFromDB())
            val processedMatchesList = processMatches(processedMatches)
            if(!processedMatchesList.isNullOrEmpty()) {
                NetworkResult.Success(processedMatchesList)
            } else {
                NetworkResult.Error(Exception(Throwable()))
            }
        } catch (exception: HttpException) {
            NetworkResult.Error(exception)
        } catch (exception: IOException) {
            NetworkResult.Error(exception)
        }
    }

    suspend fun insertMatch(match: Match) {
        matchDao.insertMatch(match)
    }

    suspend fun getMatchesFromDB(): ArrayList<Match> {
        return ArrayList(matchDao.getAllMatches())
    }

    suspend fun deleteMatchById(id: Long) {
        matchDao.deleteMatchById(id)
    }

    suspend fun getMatchesFromServer() : Response<MatchResponse> {
        return apiService.getMatches()
    }

    // Processes the matches from the server, pairs with favorite matches that saved in the database, marks isFavorite property
    // to true if the match from response is in database
    private fun addFavoriteMatches(matchResponse: MatchResponse?, favoriteMatches : ArrayList<Match>) : MatchResponse? {
        matchResponse?.matchArrayList?.let {matchArrayList ->
            matchArrayList.filter { matchInResponse ->
                favoriteMatches.any { matchInDatabase ->
                    matchInResponse.id == matchInDatabase.id
                }
            }.forEach { it.isFavorite = true }
        }
        return matchResponse
    }

    // Processes the matches, firstly, deletes matches that are not have dates and tournaments and filters out matches that are unfinished
    // after that groups the matches by tournament name, and convert the hashmap to list of grouped matches
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