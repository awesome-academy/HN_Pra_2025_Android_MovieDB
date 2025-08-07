package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.EpisodeGroup
import com.sun.moviedb.data.repository.source.remote.model.MovieDetailResponse
import org.json.JSONObject

fun parseMovieDetailResponse(json: JSONObject): MovieDetailResponse {
    val status = json.optBoolean("status", false)
    val msg = json.optString("msg", "")
    val movie = json.optJSONObject("movie")?.let { parseMovieDetail(it) }
    val episodesArr = json.optJSONArray("episodes")
    val episodes = mutableListOf<EpisodeGroup>()
    if (episodesArr != null) {
        for (i in 0 until episodesArr.length()) {
            val epGroup = episodesArr.optJSONObject(i)
            if (epGroup != null) episodes.add(parseEpisodeGroup(epGroup))
        }
    }
    return MovieDetailResponse(status, msg, movie, episodes)
}