package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.ModifiedInfo
import org.json.JSONObject

fun parseModifiedInfo(json: JSONObject): ModifiedInfo {
    return ModifiedInfo(
        time = json.optString("time")
    )
}
