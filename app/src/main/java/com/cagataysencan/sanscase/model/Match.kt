package com.cagataysencan.sanscase.model

import com.google.gson.annotations.SerializedName

data class Match(
    @SerializedName("i")
    val id : Long?,
    @SerializedName("sgi")
    val actionId : Long?,
    @SerializedName("d")
    val date: Long?,
    @SerializedName("st")
    val sport: String?,
    @SerializedName("bri")
    val reportId: Long?,
    @SerializedName("ht")
    val homeTeam: Team?,
    @SerializedName("at")
    val awayTeam: Team?,
    @SerializedName("sc")
    val score: Score?,
    @SerializedName("to")
    val tournament: Tournament?
)
