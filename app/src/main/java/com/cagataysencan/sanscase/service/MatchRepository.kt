package com.cagataysencan.sanscase.service

import com.cagataysencan.sanscase.model.MatchResponse
import retrofit2.HttpException
import java.io.IOException

class MatchRepository(val apiService: MatchAPIService) {
    suspend fun getMatches() : NetworkResult<MatchResponse?> {
        return try {
            val response = apiService.getMatches()
            NetworkResult.Success(response.body())
        } catch (e: HttpException) {
            //handles exception with the request
            NetworkResult.Error(exception = e)
        } catch (e: IOException) {
            //handles no internet exception
            NetworkResult.Error(exception = e)
        }
    }
}