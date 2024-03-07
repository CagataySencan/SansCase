package com.cagataysencan.sanscase.service

sealed class NetworkResult<T>(
    matchesList: T? = null,
    exception: Exception? = null) {
    data class Success <T>(var matchList: T) : NetworkResult<T>(matchList, null)
    data class Error <T>(val exception: Exception) : NetworkResult<T>(null, exception)
}