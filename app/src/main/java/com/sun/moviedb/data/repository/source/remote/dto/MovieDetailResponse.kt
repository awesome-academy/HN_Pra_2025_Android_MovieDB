package com.sun.moviedb.data.repository.source.remote.dto

import com.sun.moviedb.data.model.Episode
import com.sun.moviedb.data.model.Movie

data class MovieDetailResponse(
    val episodes: List<Episode>,
    val movie: Movie,
    val msg: String,
    val status: Boolean
)
