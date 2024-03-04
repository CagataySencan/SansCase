package com.cagataysencan.sanscase.service

import com.cagataysencan.sanscase.BuildConfig
import com.cagataysencan.sanscase.model.MatchResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET(BuildConfig.MATCH_URL)
    suspend fun getMatches() : Response<MatchResponse>
}