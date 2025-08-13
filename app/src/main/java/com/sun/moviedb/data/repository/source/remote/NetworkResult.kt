package com.sun.moviedb.data.repository.source.remote

sealed class NetworkResult<out T> {
    data class OnSuccess<T>(val data: T) : NetworkResult<T>()
    data class OnError(
        val code: Int?,
        val message: String
    ) : NetworkResult<Nothing>()
}
