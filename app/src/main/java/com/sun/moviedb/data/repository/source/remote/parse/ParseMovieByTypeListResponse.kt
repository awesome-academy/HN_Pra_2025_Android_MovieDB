package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.MovieByTypeListResponse
import org.json.JSONObject

fun parseMovieByTypeListResponse(json: JSONObject): MovieByTypeListResponse {
    val status = json.opt("status")
    val msg = if (json.has("msg")) json.optString("msg") else null
    val data = json.optJSONObject("data")?.let { parseMovieByTypeListData(it) }
    return MovieByTypeListResponse(status, msg, data)
}
