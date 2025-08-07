package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.MovieDetail
import org.json.JSONObject

fun parseMovieDetail(json: JSONObject): MovieDetail {
    return MovieDetail(
        tmdb = json.optJSONObject("tmdb")?.let { parseTmdbInfo(it) },
        imdb = json.optJSONObject("imdb")?.let { parseImdbInfo(it) },
        created = json.optJSONObject("created")?.let { parseCreatedInfo(it) },
        modified = json.optJSONObject("modified")?.let { parseModifiedInfo(it) },
        _id = json.optString("_id"),
        name = json.optString("name"),
        slug = json.optString("slug"),
        origin_name = json.optString("origin_name"),
        content = json.optString("content"),
        type = json.optString("type"),
        status = json.optString("status"),
        poster_url = json.optString("poster_url"),
        thumb_url = json.optString("thumb_url"),
        is_copyright = json.optBoolean("is_copyright"),
        sub_docquyen = json.optBoolean("sub_docquyen"),
        chieurap = json.optBoolean("chieurap"),
        trailer_url = json.optString("trailer_url"),
        time = json.optString("time"),
        episode_current = json.optString("episode_current"),
        episode_total = json.optString("episode_total"),
        quality = json.optString("quality"),
        lang = json.optString("lang"),
        notify = json.optString("notify"),
        showtimes = json.optString("showtimes"),
        year = if (json.has("year")) json.optInt("year") else null,
        view = if (json.has("view")) json.optInt("view") else null,
        actor = json.optJSONArray("actor")?.let { arr -> List(arr.length()) { i -> arr.optString(i) } },
        director = json.optJSONArray("director")?.let { arr -> List(arr.length()) { i -> arr.optString(i) } },
        category = json.optJSONArray("category")?.let { arr -> List(arr.length()) { i -> parseCategory(arr.getJSONObject(i)) } },
        country = json.optJSONArray("country")?.let { arr -> List(arr.length()) { i -> parseCountry(arr.getJSONObject(i)) } }
    )
}
