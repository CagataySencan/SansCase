package com.cagataysencan.sanscase.database

import com.cagataysencan.sanscase.model.Match

class DatabaseRepository(private val database: AppDatabase) {
    private val matchDao = database.getMatchDao()

    suspend fun insertMatch(match: Match) {
        matchDao.insertMatch(match)
    }

    suspend fun getAllMatches(): ArrayList<Match> {
        return ArrayList(matchDao.getAllMatches())
    }

    suspend fun deleteMatchById(id: Long) {
        matchDao.deleteMatchById(id)
    }
}