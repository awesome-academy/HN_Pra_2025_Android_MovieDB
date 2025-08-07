package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.BreadCrumb
import org.json.JSONObject

fun parseBreadCrumb(json: JSONObject): BreadCrumb {
    return BreadCrumb(
        name = json.optString("name"),
        slug = if (json.has("slug")) json.optString("slug") else null,
        isCurrent = json.optBoolean("isCurrent"),
        position = if (json.has("position")) json.optInt("position") else null
    )
}
