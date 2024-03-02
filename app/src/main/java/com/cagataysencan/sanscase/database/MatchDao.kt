package com.cagataysencan.sanscase.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.cagataysencan.sanscase.constant.Constants
import com.cagataysencan.sanscase.model.Match

@Dao
interface MatchDao {

    @Upsert
    fun insertMatch(match: Match)

    @Query("SELECT * FROM 'match'")
    fun getAllMatches(): List<Match>

    @Query("DELETE FROM 'match' WHERE id = :id")
    fun deleteMatchById(id: Long)
}