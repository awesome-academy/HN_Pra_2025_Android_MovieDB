package com.sun.moviedb.data.repository.source

import com.sun.moviedb.data.repository.source.remote.NetworkResult
import com.sun.moviedb.data.repository.source.remote.dto.DetailMovieResponse
import java.util.concurrent.Future

class MovieRepository private constructor(
    private val remote: MovieDataSource.Remote,
    private val local: MovieDataSource.Local
) : MovieDataSource.Local, MovieDataSource.Remote {

    /* *
    * LOCAL
    * */

    /* *
    * REMOTE
    * */
    override fun getDetailMovie(
        slug: String,
        callback: (NetworkResult<DetailMovieResponse>) -> Unit
    ): Future<*> = remote.getDetailMovie(slug, callback)


    companion object {
        private var instance: MovieRepository? = null

        fun getInstance(remote: MovieDataSource.Remote, local: MovieDataSource.Local) =
            synchronized(this) {
                instance ?: MovieRepository(remote, local).also { instance = it }
            }
    }
}

