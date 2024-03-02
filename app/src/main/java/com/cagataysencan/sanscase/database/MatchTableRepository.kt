package com.cagataysencan.sanscase.database

import com.cagataysencan.sanscase.model.Match

class MatchTableRepository(private val database: AppDatabase) {
    private val matchDao = database.getMatchDao()

    suspend fun insertMatch(match: Match) {
        matchDao.insertMatch(match)
    }

    suspend fun getAllMatches(): List<Match> {
        return matchDao.getAllMatches()
    }

    suspend fun deleteMatchById(id: Long) {
        matchDao.deleteMatchById(id)
    }
}