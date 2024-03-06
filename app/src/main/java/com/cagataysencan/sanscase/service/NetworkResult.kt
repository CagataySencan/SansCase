package com.cagataysencan.sanscase.service

sealed class NetworkResult<T>(
    matchesMap: T? = null,
    exception: Exception? = null) {
    data class Success <T>(var matchesMap: T) : NetworkResult<T>(matchesMap, null)
    data class Error <T>(val exception: Exception) : NetworkResult<T>(null, exception)
}