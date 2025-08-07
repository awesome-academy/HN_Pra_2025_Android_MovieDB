package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.Genre
import org.json.JSONObject

fun parseGenreItem(obj: JSONObject): Genre {
    return Genre(
        _id = obj.optString("_id"),
        name = obj.optString("name"),
        slug = obj.optString("slug")
    )
}

