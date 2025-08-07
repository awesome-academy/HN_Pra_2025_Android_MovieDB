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
        _id = if (json.has("_id")) json.optString("_id") else null,
        name = if (json.has("name")) json.optString("name") else null,
        slug = if (json.has("slug")) json.optString("slug") else null,
        origin_name = if (json.has("origin_name")) json.optString("origin_name") else null,
        type = if (json.has("type")) json.optString("type") else null,
        poster_url = if (json.has("poster_url")) fullUrl(json.optString("poster_url")) else null,
        thumb_url = if (json.has("thumb_url")) fullUrl(json.optString("thumb_url")) else null,
        sub_docquyen = if (json.has("sub_docquyen")) json.optBoolean("sub_docquyen") else null,
        chieurap = if (json.has("chieurap")) json.optBoolean("chieurap") else null,
        time = if (json.has("time")) json.optString("time") else null,
        episode_current = if (json.has("episode_current")) json.optString("episode_current") else null,
        quality = if (json.has("quality")) json.optString("quality") else null,
        lang = if (json.has("lang")) json.optString("lang") else null,
        year = if (json.has("year")) json.optInt("year") else null,
        category = json.optJSONArray("category")?.let { arr -> List(arr.length()) { i -> parseCategory(arr.getJSONObject(i)) } },
        country = json.optJSONArray("country")?.let { arr -> List(arr.length()) { i -> parseCountry(arr.getJSONObject(i)) } }
    )
}
