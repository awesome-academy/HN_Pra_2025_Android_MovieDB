package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.MovieItem
import org.json.JSONObject


fun parseMovieItem(json: JSONObject, domain: String? = null): MovieItem {
    fun fullUrl(url: String?): String? {
        if (url.isNullOrEmpty()) return url
        return if (url.startsWith("http")) url else domain?.let { if (it.endsWith("/")) it + url else "$it/$url" } ?: url
    }
    return MovieItem(
        tmdb = json.optJSONObject("tmdb")?.let { parseTmdbInfo(it) },
        imdb = json.optJSONObject("imdb")?.let { parseImdbInfo(it) },
        created = json.optJSONObject("created")?.let { parseCreatedInfo(it) },
        modified = json.optJSONObject("modified")?.let { parseModifiedInfo(it) },
        _id = json.optString("_id"),
        name = json.optString("name"),
        slug = json.optString("slug"),
        origin_name = json.optString("origin_name"),
        type = json.optString("type"),
        poster_url = fullUrl(json.optString("poster_url")),
        thumb_url = fullUrl(json.optString("thumb_url")),
        sub_docquyen = json.optBoolean("sub_docquyen"),
        chieurap = if (json.has("chieurap")) json.optBoolean("chieurap") else null,
        time = json.optString("time"),
        episode_current = json.optString("episode_current"),
        quality = json.optString("quality"),
        lang = json.optString("lang"),
        year = if (json.has("year")) json.optInt("year") else null,
        category = json.optJSONArray("category")?.let { arr ->
            List(arr.length()) { i -> parseCategory(arr.getJSONObject(i)) }
        },
        country = json.optJSONArray("country")?.let { arr ->
            List(arr.length()) { i -> parseCountry(arr.getJSONObject(i)) }
        }
    )
}

