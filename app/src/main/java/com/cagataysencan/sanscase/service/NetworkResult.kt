package com.cagataysencan.sanscase.service

sealed class NetworkResult<T>(
    data: T? = null,
    exception: Exception? = null) {
    data class Success <T>(val data: T) : NetworkResult<T>(data, null)
    data class Error <T>(val exception: Exception) : NetworkResult<T>(null, exception)
}