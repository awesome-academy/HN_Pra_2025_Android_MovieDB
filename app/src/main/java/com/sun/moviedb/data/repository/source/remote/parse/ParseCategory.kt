package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.Category
import org.json.JSONObject

fun parseCategory(json: JSONObject): Category {
    return Category(
        id = if (json.has("id")) json.optString("id") else null,
        name = if (json.has("name")) json.optString("name") else null,
        slug = if (json.has("slug")) json.optString("slug") else null
    )
}
