package com.cagataysencan.sanscase.database

import com.cagataysencan.sanscase.model.Match
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val matchDao: BaseDao) {

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