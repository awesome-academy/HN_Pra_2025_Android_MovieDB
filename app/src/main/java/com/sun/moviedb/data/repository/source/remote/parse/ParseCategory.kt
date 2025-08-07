package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.Category
import org.json.JSONObject

fun parseCategory(json: JSONObject): Category {
    return Category(
        id = json.optString("id"),
        name = json.optString("name"),
        slug = json.optString("slug")
    )
}
