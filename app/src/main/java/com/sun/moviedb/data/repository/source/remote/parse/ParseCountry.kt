package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.Country
import org.json.JSONObject

fun parseCountry(json: JSONObject): Country {
    return Country(
        id = json.optString("id"),
        name = json.optString("name"),
        slug = json.optString("slug")
    )
}
