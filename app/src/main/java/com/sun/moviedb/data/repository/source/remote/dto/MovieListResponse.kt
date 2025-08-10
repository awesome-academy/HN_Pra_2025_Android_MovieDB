package com.sun.moviedb.data.repository.source.remote.dto

import com.sun.moviedb.data.model.Item
import com.sun.moviedb.data.model.Pagination

data class MovieListResponse(
    val items: List<Item>,
    val msg: String,
    val pagination: Pagination,
    val status: Boolean
)

