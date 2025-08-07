package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.MovieResponse
import com.sun.moviedb.data.repository.source.remote.model.MovieItem
import org.json.JSONObject

fun parseMovieResponse(json: JSONObject): MovieResponse {
    val status = json.optBoolean("status", false)
    val msg = json.optString("msg", "")
    val itemsJson = json.optJSONArray("items")
    val items = mutableListOf<MovieItem>()
    if (itemsJson != null) {
        for (i in 0 until itemsJson.length()) {
            val itemJson = itemsJson.optJSONObject(i)
            if (itemJson != null) items.add(parseMovieItem(itemJson))
        }
    }
    val pagination = json.optJSONObject("pagination")?.let { parsePagination(it) }
    return MovieResponse(status, msg, items, pagination)
}


