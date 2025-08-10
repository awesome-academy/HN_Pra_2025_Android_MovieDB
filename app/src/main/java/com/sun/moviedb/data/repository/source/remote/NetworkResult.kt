package com.sun.moviedb.data.repository.source.remote

sealed class NetworkResult<out T> {
    data class onSuccess<T>(val data: T) : NetworkResult<T>()
    data class onError(
        val code: Int?,
        val message: String
    ) : NetworkResult<Nothing>()
}