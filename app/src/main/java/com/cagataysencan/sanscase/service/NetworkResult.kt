package com.cagataysencan.sanscase.service
// Represents the result of a network request. The class has two subclasses: Success and Error.
// All possible outcomes of the network request are handled within the Success or Error subclasses.
sealed class NetworkResult<T>(
    matchesList: T? = null,
    exception: Exception? = null) {
    data class Success <T>(val matchList: T) : NetworkResult<T>(matchList, null)
    data class Error <T>(val exception: Exception) : NetworkResult<T>(null, exception)
}