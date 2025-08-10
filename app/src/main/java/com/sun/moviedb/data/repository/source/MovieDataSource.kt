package com.sun.moviedb.data.repository.source

import com.sun.moviedb.data.repository.source.remote.NetworkResult
import com.sun.moviedb.data.repository.source.remote.dto.DetailMovieResponse
import java.util.concurrent.Future

interface MovieDataSource {

    interface Local{

    }

    interface Remote{
        fun getDetailMovie(
            slug: String,
            callback: (NetworkResult<DetailMovieResponse>) ->Unit,
        ) : Future<*>
    }
}