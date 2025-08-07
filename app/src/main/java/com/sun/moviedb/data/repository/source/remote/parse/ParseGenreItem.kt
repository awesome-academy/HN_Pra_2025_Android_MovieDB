package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.Genre
import org.json.JSONObject

fun parseGenreItem(obj: JSONObject): Genre {
    return Genre(
        _id = if (obj.has("_id")) obj.optString("_id") else null,
        name = if (obj.has("name")) obj.optString("name") else null,
        slug = if (obj.has("slug")) obj.optString("slug") else null
    )
}
