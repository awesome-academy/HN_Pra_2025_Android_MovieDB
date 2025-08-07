package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.Area
import org.json.JSONObject

fun parseAreaItem(obj: JSONObject): Area {
    return Area(
        _id = if (obj.has("_id")) obj.optString("_id") else null,
        name = if (obj.has("name")) obj.optString("name") else null,
        slug = if (obj.has("slug")) obj.optString("slug") else null
    )
}
