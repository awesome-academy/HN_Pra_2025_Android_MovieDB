package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.Country
import org.json.JSONObject

fun parseCountry(json: JSONObject): Country {
    return Country(
        id = if (json.has("id")) json.optString("id") else null,
        name = if (json.has("name")) json.optString("name") else null,
        slug = if (json.has("slug")) json.optString("slug") else null
    )
}
