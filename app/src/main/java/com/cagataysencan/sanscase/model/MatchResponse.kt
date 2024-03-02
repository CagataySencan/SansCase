package com.cagataysencan.sanscase.model

import com.google.gson.annotations.SerializedName

data class MatchResponse(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("data")
    val data: List<Match>?
)
