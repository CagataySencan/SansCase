package com.cagataysencan.sanscase.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cagataysencan.sanscase.constant.Constants
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = Constants.TABLE_NAME_MATCH)
data class Match(
    @PrimaryKey(autoGenerate = false)
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
) : Serializable
