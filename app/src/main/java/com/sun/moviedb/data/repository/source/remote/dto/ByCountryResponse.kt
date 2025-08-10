package com.sun.moviedb.data.repository.source.remote.dto

import com.sun.moviedb.data.model.Data

data class ByCountryResponse(
    val data: Data,
    val msg: String,
    val status: String
)
