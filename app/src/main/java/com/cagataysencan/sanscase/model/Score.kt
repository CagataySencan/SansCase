package com.cagataysencan.sanscase.model

import com.google.gson.annotations.SerializedName

data class Score(
    @SerializedName("st")
    val scoreType: Int?,
    @SerializedName("abbr")
    val abbreviation: String?,
    @SerializedName("ht")
    val homeScore: TeamScore?,
    @SerializedName("at")
    val awayScore: TeamScore?
)
