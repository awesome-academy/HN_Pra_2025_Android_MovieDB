package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.BreadCrumb
import org.json.JSONObject

fun parseBreadCrumb(json: JSONObject): BreadCrumb {
    return BreadCrumb(
        name = if (json.has("name")) json.optString("name") else null,
        slug = if (json.has("slug")) json.optString("slug") else null,
        isCurrent = if (json.has("isCurrent")) json.optBoolean("isCurrent") else null,
        position = if (json.has("position")) json.optInt("position") else null
    )
}
