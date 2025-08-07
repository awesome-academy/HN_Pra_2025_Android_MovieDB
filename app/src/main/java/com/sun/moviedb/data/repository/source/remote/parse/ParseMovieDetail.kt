package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.MovieDetail
import org.json.JSONObject

fun parseMovieDetail(json: JSONObject): MovieDetail {
    return MovieDetail(
        tmdb = json.optJSONObject("tmdb")?.let { parseTmdbInfo(it) },
        imdb = json.optJSONObject("imdb")?.let { parseImdbInfo(it) },
        created = json.optJSONObject("created")?.let { parseCreatedInfo(it) },
        modified = json.optJSONObject("modified")?.let { parseModifiedInfo(it) },
        _id = if (json.has("_id")) json.optString("_id") else null,
        name = if (json.has("name")) json.optString("name") else null,
        slug = if (json.has("slug")) json.optString("slug") else null,
        origin_name = if (json.has("origin_name")) json.optString("origin_name") else null,
        content = if (json.has("content")) json.optString("content") else null,
        type = if (json.has("type")) json.optString("type") else null,
        status = if (json.has("status")) json.optString("status") else null,
        poster_url = if (json.has("poster_url")) json.optString("poster_url") else null,
        thumb_url = if (json.has("thumb_url")) json.optString("thumb_url") else null,
        is_copyright = if (json.has("is_copyright")) json.optBoolean("is_copyright") else null,
        sub_docquyen = if (json.has("sub_docquyen")) json.optBoolean("sub_docquyen") else null,
        chieurap = if (json.has("chieurap")) json.optBoolean("chieurap") else null,
        trailer_url = if (json.has("trailer_url")) json.optString("trailer_url") else null,
        time = if (json.has("time")) json.optString("time") else null,
        episode_current = if (json.has("episode_current")) json.optString("episode_current") else null,
        episode_total = if (json.has("episode_total")) json.optString("episode_total") else null,
        quality = if (json.has("quality")) json.optString("quality") else null,
        lang = if (json.has("lang")) json.optString("lang") else null,
        notify = if (json.has("notify")) json.optString("notify") else null,
        showtimes = if (json.has("showtimes")) json.optString("showtimes") else null,
        year = if (json.has("year")) json.optInt("year") else null,
        view = if (json.has("view")) json.optInt("view") else null,
        actor = json.optJSONArray("actor")?.let { arr -> List(arr.length()) { i -> arr.optString(i) } },
        director = json.optJSONArray("director")?.let { arr -> List(arr.length()) { i -> arr.optString(i) } },
        category = json.optJSONArray("category")?.let { arr -> List(arr.length()) { i -> parseCategory(arr.getJSONObject(i)) } },
        country = json.optJSONArray("country")?.let { arr -> List(arr.length()) { i -> parseCountry(arr.getJSONObject(i)) } }
    )
}
