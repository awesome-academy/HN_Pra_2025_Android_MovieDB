package com.sun.moviedb.data.repository.source.remote.parse

import org.json.JSONArray
import org.json.JSONObject
import com.sun.moviedb.data.model.*
import com.sun.moviedb.data.repository.source.remote.dto.DetailMovieResponse

/* *
* JSON helpers (extensions)
* */
private inline fun <T> JSONArray.mapObjects(mapper: (JSONObject) -> T): List<T> {
    val out = ArrayList<T>(length())
    for (i in 0 until length()) {
        val o = optJSONObject(i) ?: continue
        out.add(mapper(o))
    }
    return out
}

private fun JSONObject.optBooleanCoerce(key: String, default: Boolean = false): Boolean {
    return when (val v = opt(key)) {
        is Boolean -> v
        is String  -> v.equals("true", true) || v == "1"
        is Number  -> v.toInt() != 0
        else       -> default
    }
}

fun JSONObject.optStringMulti(vararg keys: String, default: String = ""): String {
    for (k in keys) {
        when (val v = this.opt(k)) {
            is String -> if (v.isNotBlank()) return v
            is Number -> return v.toString()
            is Boolean -> return v.toString()
        }
    }
    return default
}

/* *
* Model parsers (extensions)
* */
fun JSONObject.toCategory(): Category = Category(
    id   = optStringMulti("id", "_id"),
    name = optString("name"),
    slug = optString("slug")
)

fun JSONArray.toCategoryList(): List<Category> = mapObjects { it.toCategory() }

fun JSONObject.toCountry(): Country = Country(
    id   = optStringMulti("id", "_id"),
    name = optString("name"),
    slug = optString("slug")
)

fun JSONArray.toCountryList(): List<Country> = mapObjects { it.toCountry() }

fun JSONArray.toStringList(): List<String> {
    val out = ArrayList<String>(length())
    for (i in 0 until length()) {
        val s = optString(i, null)
        if (s != null) out.add(s)
    }
    return out
}

fun JSONObject.toServerData(): ServerData = ServerData(
    filename  = optString("filename"),
    link_embed= optString("link_embed"),
    link_m3u8 = optString("link_m3u8"),
    name      = optString("name"),
    slug      = optString("slug")
)

fun JSONArray.toServerDataList(): List<ServerData> = mapObjects { it.toServerData() }

fun JSONObject.toEpisode(): Episode = Episode(
    server_name = optString("server_name"),
    server_data = optJSONArray("server_data")?.toServerDataList() ?: emptyList()
)

fun JSONArray.toEpisodeList(): List<Episode> = mapObjects { it.toEpisode() }

fun JSONObject.toMovie(): Movie = Movie(
    _id             = optString("_id"),
    actor           = optJSONArray("actor")?.toStringList() ?: emptyList(),
    category        = optJSONArray("category")?.toCategoryList() ?: emptyList(),
    chieurap        = optBoolean("chieurap", false),
    content         = optString("content"),
    country         = optJSONArray("country")?.toCountryList() ?: emptyList(),
    director        = optJSONArray("director")?.toStringList() ?: emptyList(),
    episode_current = optString("episode_current"),
    episode_total   = optString("episode_total"),
//    is_copyright    = optBoolean("is_copyright", false),
    lang            = optString("lang"),
    name            = optString("name"),
//    notify          = optString("notify"),
    origin_name     = optString("origin_name"),
    poster_url      = optString("poster_url"),
    quality         = optString("quality"),
//    showtimes       = optString("showtimes"),
    slug            = optString("slug"),
    status          = optString("status"),
//    sub_docquyen    = optBoolean("sub_docquyen", false),
    thumb_url       = optString("thumb_url"),
    time            = optString("time"),
//    trailer_url     = optString("trailer_url"),
    type            = optString("type"),
    view            = optInt("view", 0),
    year            = optInt("year", 0)
)

/* *
* DetailMovieResponse parser (extensions)
* */
fun String.toDetailMovieResponse(strict: Boolean = false): DetailMovieResponse {
    val root = JSONObject(this)

    val status = root.optBooleanCoerce("status", false)
    val msg = root.optString("msg")

    val movie = when (val movieAny = root.opt("movie")) {
        is JSONObject -> movieAny.toMovie()
        null, JSONObject.NULL, is String -> if (strict) {
            throw IllegalArgumentException("Invalid schema: movie")
        } else Movie()
        else -> if (strict) throw IllegalArgumentException("Invalid schema: movie(type)") else Movie()
    }

    val episodes = when (val epsAny = root.opt("episodes")) {
        is JSONArray -> epsAny.toEpisodeList()
        null, JSONObject.NULL, is String -> if (strict) {
            throw IllegalArgumentException("Invalid schema: episodes")
        } else emptyList()
        else -> if (strict) throw IllegalArgumentException("Invalid schema: episodes(type)") else emptyList()
    }

    return DetailMovieResponse(
        episodes = episodes,
        movie = movie,
        msg = msg,
        status = status
    )
}
