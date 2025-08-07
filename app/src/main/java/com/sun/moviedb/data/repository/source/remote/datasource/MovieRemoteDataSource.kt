package com.sun.moviedb.data.repository.source.remote.datasource

import com.sun.moviedb.data.repository.source.remote.model.MovieDetailResponse
import com.sun.moviedb.data.repository.source.remote.api.ApiEndpoint
import com.sun.moviedb.data.repository.source.remote.model.Area
import com.sun.moviedb.data.repository.source.remote.model.MovieByTypeListResponse
import com.sun.moviedb.data.repository.source.remote.model.MovieResponse
import com.sun.moviedb.data.repository.source.remote.parse.parseMovieByTypeListResponse
import com.sun.moviedb.data.repository.source.remote.parse.parseMovieDetailResponse
import com.sun.moviedb.data.repository.source.remote.parse.parseMovieResponse
import com.sun.moviedb.data.repository.source.remote.model.Genre
import com.sun.moviedb.data.repository.source.remote.parse.parseGenreItem
import com.sun.moviedb.data.repository.source.remote.parse.parseAreaItem
import com.sun.moviedb.utils.network.ApiHelper

class MovieRemoteDataSource {
    fun getNewestMovies(page: Int, callback: ApiHelper.Callback<MovieResponse>) {
        val url = "${ApiEndpoint.GET_NEWEST_MOVIE}?page=$page"
        ApiHelper.fetchObjectAsync(url, { json -> parseMovieResponse(json) }, callback)
    }

    fun getMovieDetail(slug: String, callback: ApiHelper.Callback<MovieDetailResponse>) {
        val url = ApiEndpoint.GET_MOVIE_DETAIL + slug
        ApiHelper.fetchObjectAsync(url, { json -> parseMovieDetailResponse(json) }, callback)
    }

    fun getMoviesByTypeList(typeList: String, page: Int, limit: Int, callback: ApiHelper.Callback<MovieByTypeListResponse>) {
        val url = "${ApiEndpoint.GET_MOVIE_BY_TYPE_LIST}$typeList?page=$page&limit=$limit"
        ApiHelper.fetchObjectAsync(url, { json -> parseMovieByTypeListResponse(json) }, callback)
    }

    fun getMoviesByGenre(
        typeList: String,
        page: Int,
        limit: Int,
        sortField: String? = null,
        sortType: String? = null,
        sortLang: String? = null,
        country: String? = null,
        year: Int? = null,
        callback: ApiHelper.Callback<MovieByTypeListResponse>
    ) {
        val params = mutableListOf(
            "page=$page",
            "limit=$limit"
        )
        sortField?.let { params.add("sort_field=$it") }
        sortType?.let { params.add("sort_type=$it") }
        sortLang?.let { params.add("sort_lang=$it") }
        country?.let { params.add("country=$it") }
        year?.let { params.add("year=$it") }
        val url = "${ApiEndpoint.GET_MOVIE_BY_GENRE}$typeList?${params.joinToString("&")}"
        ApiHelper.fetchObjectAsync(url, { json -> parseMovieByTypeListResponse(json) }, callback)
    }

    fun searchMovies(keyword: String, page: Int, limit: Int, callback: ApiHelper.Callback<MovieByTypeListResponse>) {
        val url = "${ApiEndpoint.SEARCH_MOVIE}?keyword=$keyword&page=$page&limit=$limit"
        ApiHelper.fetchObjectAsync(url, { json -> parseMovieByTypeListResponse(json) }, callback)
    }

    fun getGenres(callback: ApiHelper.Callback<List<Genre>>) {
        val url = ApiEndpoint.GET_GENRES
        ApiHelper.fetchListAsync(url, { obj -> parseGenreItem(obj) }, callback)
    }

    fun getAreas(callback: ApiHelper.Callback<List<Area>>) {
        val url = ApiEndpoint.GET_COUNTRIES
        ApiHelper.fetchListAsync(url, { obj -> parseAreaItem(obj) }, callback)
    }
}
