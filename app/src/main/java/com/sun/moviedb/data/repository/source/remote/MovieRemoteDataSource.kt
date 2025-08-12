package com.sun.moviedb.data.repository.source.remote

import android.util.Log
import com.sun.moviedb.data.model.Category
import com.sun.moviedb.data.model.Country
import com.sun.moviedb.data.repository.source.MovieDataSource
import com.sun.moviedb.data.repository.source.remote.api.Endpoint
import com.sun.moviedb.data.repository.source.remote.dto.MovieDetailResponse
import com.sun.moviedb.data.repository.source.remote.dto.MovieListResponse
import com.sun.moviedb.data.repository.source.remote.parse.toCategoryList
import com.sun.moviedb.data.repository.source.remote.parse.toCountryList
import com.sun.moviedb.data.repository.source.remote.parse.toDetailMovieResponse
import com.sun.moviedb.data.repository.source.remote.parse.toMovieListResponse
import com.sun.moviedb.utils.network.ApiHelper
import java.util.concurrent.Future

class MovieRemoteDataSource : MovieDataSource.Remote {

    override fun getDetailMovie(
        slug: String,
        callback: (NetworkResult<MovieDetailResponse>) -> Unit
    ): Future<*> {
        val urlString = Endpoint.GET_MOVIE_DETAIL + slug
        return ApiHelper.getResultFromUrlAsync(
            urlString,
            { body -> body.toDetailMovieResponse() },
            callback
        )
    }

    override fun getNewestMovie(
        page: Int,
        callback: (NetworkResult<MovieListResponse>) -> Unit
    ): Future<*> {
        val urlString = Endpoint.GET_NEWEST_MOVIE + "?page=$page"
        return ApiHelper.getResultFromUrlAsync(
            urlString,
            { body -> body.toMovieListResponse() },
            callback
        )
    }

    override fun getSeriesMovie(
        typeList: String,
        page: Int,
        limit: Int,
        callback: (NetworkResult<MovieListResponse>) -> Unit
    ): Future<*> {
        val urlString = Endpoint.GET_SERIES_MOVIE + typeList + "/?page=$page&limit=$limit"
        return ApiHelper.getResultFromUrlAsync(
            urlString,
            { body -> body.toMovieListResponse() },
            callback
        )
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
        return ApiHelper.getResultFromUrlAsync(
            urlString,
            { body -> body.toMovieListResponse() },
            callback
        )
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
        return ApiHelper.getResultFromUrlAsync(
            urlString,
            { body -> body.toMovieListResponse() },
            callback
        )
    }

    override fun getCategories(callback: (NetworkResult<List<Category>>) -> Unit): Future<*> {
        val urlString = Endpoint.GET_CATEGORIES
        return ApiHelper.getResultFromUrlAsync(urlString, { body ->
            body.toCategoryList()
        }, callback)
    }

    override fun getCountries(callback: (NetworkResult<List<Country>>) -> Unit): Future<*> {
        val urlString = Endpoint.GET_COUNTRIES
        return ApiHelper.getResultFromUrlAsync(
            urlString,
            { body -> body.toCountryList() },
            callback
        )
    }

    companion object {
        private var instance: MovieRemoteDataSource? = null

        fun getInstance() = synchronized(this) {
            instance ?: MovieRemoteDataSource().also { instance = it }
        }
    }
}
