package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.Area
import org.json.JSONObject

fun parseAreaItem(obj: JSONObject): Area {
    return Area(
        _id = obj.optString("_id"),
        name = obj.optString("name"),
        slug = obj.optString("slug")
    )
}

