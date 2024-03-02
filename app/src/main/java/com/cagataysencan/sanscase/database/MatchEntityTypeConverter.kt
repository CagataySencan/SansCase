package com.cagataysencan.sanscase.database

import androidx.room.TypeConverter
import com.cagataysencan.sanscase.model.Score
import com.cagataysencan.sanscase.model.Team
import com.cagataysencan.sanscase.model.TeamScore
import com.cagataysencan.sanscase.model.Tournament
import com.google.gson.Gson

class MatchEntityTypeConverter {
    @TypeConverter
    fun fromTeam(team: Team?): String? {
        return Gson().toJson(team)
    }

    @TypeConverter
    fun toTeam(json: String?): Team? {
        return Gson().fromJson(json, Team::class.java)
    }

    @TypeConverter
    fun fromScore(score: Score?): String? {
        return Gson().toJson(score)
    }

    @TypeConverter
    fun toScore(json: String?): Score? {
        return Gson().fromJson(json, Score::class.java)
    }

    @TypeConverter
    fun fromTournament(tournament: Tournament?): String? {
        return Gson().toJson(tournament)
    }

    @TypeConverter
    fun toTournament(json: String?): Tournament? {
        return Gson().fromJson(json, Tournament::class.java)
    }


    @TypeConverter
    fun fromTeamScore(teamScore: TeamScore?): String? {
        return Gson().toJson(teamScore)
    }

    @TypeConverter
    fun toTeamScore(json: String?): TeamScore? {
        return Gson().fromJson(json, TeamScore::class.java)
    }
}