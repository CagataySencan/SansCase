package com.cagataysencan.sanscase.model

import com.google.gson.annotations.SerializedName

data class Tournament(
    @SerializedName("i")
    val id: Long?,
    @SerializedName("n")
    val name: String?,
    @SerializedName("sn")
    val shortName: String?,
    @SerializedName("p")
    val point: Int?,
    @SerializedName("flag")
    val flag: String?
)
