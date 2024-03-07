package com.cagataysencan.sanscase.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.cagataysencan.sanscase.model.Match

@Dao
interface MatchDao {
    @Upsert
    suspend fun insertMatch(match: Match)

    @Query("SELECT * FROM 'match'")
    suspend fun getAllMatches(): List<Match>

    @Query("DELETE FROM 'match' WHERE id = :id")
    suspend fun deleteMatchById(id: Long)
}