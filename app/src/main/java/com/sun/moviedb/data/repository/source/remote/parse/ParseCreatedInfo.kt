package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.CreatedInfo
import org.json.JSONObject

fun parseCreatedInfo(json: JSONObject): CreatedInfo {
    return CreatedInfo(
        time = json.optString("time")
    )
}
