package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.MovieResponse
import com.sun.moviedb.data.repository.source.remote.model.MovieItem
import org.json.JSONObject

fun parseMovieResponse(json: JSONObject): MovieResponse {
    val status = if (json.has("status")) json.optBoolean("status") else null
    val msg = if (json.has("msg")) json.optString("msg") else null
    val itemsJson = json.optJSONArray("items")
    val items = itemsJson?.let { arr -> List(arr.length()) { i -> arr.optJSONObject(i)?.let { parseMovieItem(it) } }.filterNotNull() }
    val pagination = json.optJSONObject("pagination")?.let { parsePagination(it) }
    return MovieResponse(status, msg, items, pagination)
}
