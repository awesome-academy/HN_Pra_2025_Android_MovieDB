package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.dto.MovieListResponse
import org.json.JSONObject

fun String.toMovieListResponse(): MovieListResponse {
    val root = JSONObject(this)
    val status = root.optBoolean("status", false)
    val msg = root.optString("msg")
    val dataObj = root.optJSONObject("data")
    val items = dataObj?.optJSONArray("items")?.let { arr ->
        (0 until arr.length()).map { i -> arr.getJSONObject(i).toItem() }
    } ?: root.optJSONArray("items")?.let { arr ->
        (0 until arr.length()).map { i -> arr.getJSONObject(i).toItem() }
    } ?: emptyList()
    val pagination = dataObj?.optJSONObject("params")?.optJSONObject("pagination")?.toPagination()
        ?: root.optJSONObject("pagination")?.toPagination()
        ?: com.sun.moviedb.data.model.Pagination()
    return MovieListResponse(
        items = items,
        msg = msg,
        pagination = pagination,
        status = status
    )
}

