package com.cagataysencan.sanscase.model

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("i")
    val id: Long?,
    @SerializedName("n")
    val name: String?,
    @SerializedName("p")
    val point: Int?,
    @SerializedName("sn")
    val shortName: String?,
    @SerializedName("rc")
    val redCard: Int?,
)