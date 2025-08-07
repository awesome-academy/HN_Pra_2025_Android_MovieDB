package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.EpisodeGroup
import com.sun.moviedb.data.repository.source.remote.model.MovieDetailResponse
import org.json.JSONObject

fun parseMovieDetailResponse(json: JSONObject): MovieDetailResponse {
    val status = if (json.has("status")) json.optBoolean("status") else null
    val msg = if (json.has("msg")) json.optString("msg") else null
    val movie = json.optJSONObject("movie")?.let { parseMovieDetail(it) }
    val episodesArr = json.optJSONArray("episodes")
    val episodes = episodesArr?.let { arr -> List(arr.length()) { i -> arr.optJSONObject(i)?.let { parseEpisodeGroup(it) } }.filterNotNull() }
    return MovieDetailResponse(status, msg, movie, episodes)
}