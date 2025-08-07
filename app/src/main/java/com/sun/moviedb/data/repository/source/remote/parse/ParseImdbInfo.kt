package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.ImdbInfo
import org.json.JSONObject

fun parseImdbInfo(json: JSONObject): ImdbInfo {
    return ImdbInfo(
        id = if (json.has("id")) json.optString("id") else null
    )
}
