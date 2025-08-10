package com.sun.moviedb.data.repository.source.remote

import android.os.Handler
import android.os.Looper
import com.sun.moviedb.data.repository.source.MovieDataSource
import com.sun.moviedb.data.repository.source.remote.api.Endpoint
import com.sun.moviedb.data.repository.source.remote.dto.MovieDetailResponse
import com.sun.moviedb.data.repository.source.remote.dto.MovieListResponse
import com.sun.moviedb.data.repository.source.remote.parse.toDetailMovieResponse
import com.sun.moviedb.data.repository.source.remote.parse.toMovieListResponse
import com.sun.moviedb.utils.network.ApiHelper
import java.util.concurrent.Executors
import java.util.concurrent.Future

class MovieRemoteDataSource : MovieDataSource.Remote {
    private val executor = Executors.newSingleThreadExecutor()
    private val mainThread = Handler(Looper.getMainLooper())

    override fun getDetailMovie(
        slug: String,
        callback: (NetworkResult<MovieDetailResponse>) -> Unit
    ): Future<*> {
        val urlString = Endpoint.GET_MOVIE_DETAIL + slug

        return executor.submit {
            val result: NetworkResult<MovieDetailResponse> = try {
                ApiHelper.getObjectFromUrl(
                    urlString
                ) { body -> body.toDetailMovieResponse() }
            } catch (e: Exception) {
                NetworkResult.OnError(9999, e.message ?: "Unknown error")
            }

            mainThread.post {
                callback(result)
            }
        }
    }

    override fun getNewestMovie(
        page: Int,
        callback: (NetworkResult<MovieListResponse>) -> Unit
    ): Future<*> {
        val urlString = Endpoint.GET_NEWEST_MOVIE + "?page=$page"
        return executor.submit {
            val result: NetworkResult<MovieListResponse> = try {
                ApiHelper.getObjectFromUrl(
                    urlString
                ) { body -> body.toMovieListResponse() }
            } catch (e: Exception) {
                NetworkResult.OnError(9999, e.message ?: "Unknown error")
            }
            mainThread.post {
                callback(result)
            }
        }
    }

    override fun getSeriesMovie(
        typeList: String,
        page: Int,
        limit: Int,
        callback: (NetworkResult<MovieListResponse>) -> Unit
    ): Future<*> {
        val urlString = Endpoint.GET_SERIES_MOVIE + typeList + "/?page=$page&limit=$limit"
        return executor.submit {
            val result: NetworkResult<MovieListResponse> = try {
                ApiHelper.getObjectFromUrl(
                    urlString
                ) { body -> body.toMovieListResponse() }
            } catch (e: Exception) {
                NetworkResult.OnError(9999, e.message ?: "Unknown error")
            }
            mainThread.post {
                callback(result)
            }
        }
    }


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
    ): Future<*> {
        val params = mutableListOf(
            "page=$page",
            "sort_field=$sortField",
            "sort_type=$sortType",
            "limit=$limit"
        )
        sortLang?.let { if (it.isNotBlank()) params.add("sort_lang=$it") }
        country?.let { if (it.isNotBlank()) params.add("country=$it") }
        year?.let { if (it.isNotBlank()) params.add("year=$it") }
        val urlString = Endpoint.GET_FILTER_MOVIE + typeList + "?" + params.joinToString("&")
        return executor.submit {
            val result: NetworkResult<MovieListResponse> = try {
                ApiHelper.getObjectFromUrl(
                    urlString
                ) { body -> body.toMovieListResponse() }
            } catch (e: Exception) {
                NetworkResult.OnError(9999, e.message ?: "Unknown error")
            }
            mainThread.post {
                callback(result)
            }
        }
    }

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
    ): Future<*> {
        val params = mutableListOf(
            "keyword=$keyword",
            "page=$page",
            "sort_field=$sortField",
            "sort_type=$sortType",
            "limit=$limit"
        )
        sortLang?.let { if (it.isNotBlank()) params.add("sort_lang=$it") }
        category?.let { if (it.isNotBlank()) params.add("category=$it") }
        country?.let { if (it.isNotBlank()) params.add("country=$it") }
        year?.let { if (it.isNotBlank()) params.add("year=$it") }
        val urlString = Endpoint.SEARCH_MOVIE + "?" + params.joinToString("&")
        return executor.submit {
            val result: NetworkResult<MovieListResponse> = try {
                ApiHelper.getObjectFromUrl(
                    urlString
                ) { body -> body.toMovieListResponse() }
            } catch (e: Exception) {
                NetworkResult.OnError(9999, e.message ?: "Unknown error")
            }
            mainThread.post {
                callback(result)
            }
        }
    }

    companion object {
        private var instance: MovieRemoteDataSource? = null

        fun getInstance() = synchronized(this) {
            instance ?: MovieRemoteDataSource().also { instance = it }
        }
    }
}
