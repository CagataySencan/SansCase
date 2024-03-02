package com.cagataysencan.sanscase.service

import com.cagataysencan.sanscase.BuildConfig
import com.cagataysencan.sanscase.model.MatchResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MatchApiService {
    private val retrofitAPI = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MatchApi::class.java)

    suspend fun getMatches(): Response<MatchResponse> {
        return retrofitAPI.getMatches()
    }
}