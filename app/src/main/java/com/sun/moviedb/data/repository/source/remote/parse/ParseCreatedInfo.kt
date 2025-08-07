package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.CreatedInfo
import org.json.JSONObject

fun parseCreatedInfo(json: JSONObject): CreatedInfo {
    return CreatedInfo(
        time = if (json.has("time")) json.optString("time") else null
    )
}
