package com.sun.moviedb.data.repository.source

import com.sun.moviedb.data.repository.source.remote.NetworkResult
import com.sun.moviedb.data.repository.source.remote.dto.MovieDetailResponse
import com.sun.moviedb.data.repository.source.remote.dto.MovieListResponse
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
        callback: (NetworkResult<MovieDetailResponse>) -> Unit
    ): Future<*> = remote.getDetailMovie(slug, callback)

    override fun getNewestMovie(
        page: Int,
        callback: (NetworkResult<MovieListResponse>) -> Unit
    ): Future<*> = remote.getNewestMovie(page, callback)

    override fun getSeriesMovie(
        typeList: String,
        page: Int,
        limit: Int,
        callback: (NetworkResult<MovieListResponse>) -> Unit
    ): Future<*> = remote.getSeriesMovie(typeList, page, limit, callback)

    override fun getFilterMovie(
        typeList: String,
        page: Int,
        limit: Int,
        sortField: String,
        sortType: String,
        sortLang: String?,
        country: String?,
        year: String?,
        callback: (NetworkResult<MovieListResponse>) -> Unit
    ): Future<*> = remote.getFilterMovie(
        typeList, page, limit, sortField, sortType, sortLang, country, year, callback
    )

    override fun searchMovie(
        keyword: String,
        page: Int,
        limit: Int,
        sortField: String,
        sortType: String,
        sortLang: String?,
        category: String?,
        country: String?,
        year: String?,
        callback: (NetworkResult<MovieListResponse>) -> Unit
    ): Future<*> = remote.searchMovie(
        keyword,
        page,
        limit,
        sortField,
        sortType,
        sortLang,
        category,
        country,
        year,
        callback
    )


    companion object {
        private var instance: MovieRepository? = null

        fun getInstance(remote: MovieDataSource.Remote, local: MovieDataSource.Local) =
            synchronized(this) {
                instance ?: MovieRepository(remote, local).also { instance = it }
            }
    }
}

