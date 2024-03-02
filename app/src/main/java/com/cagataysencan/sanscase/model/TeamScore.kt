package com.cagataysencan.sanscase.model

import com.google.gson.annotations.SerializedName

data class TeamScore(
    @SerializedName("r")
    val score: Int?,
    @SerializedName("c")
    val goalCount: Int?,
    @SerializedName("ht")
    val firstHalfScore: Int?,
)
