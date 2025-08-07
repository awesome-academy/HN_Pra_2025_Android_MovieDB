package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.Params
import org.json.JSONObject

fun parseParams(json: JSONObject): Params {
    return Params(
        type_slug = json.optString("type_slug"),
        slug = if (json.has("slug")) json.optString("slug") else null,
        keyword = if (json.has("keyword")) json.optString("keyword") else null,
        filterCategory = json.optJSONArray("filterCategory")?.let { arr -> List(arr.length()) { i -> arr.optString(i) } },
        filterCountry = json.optJSONArray("filterCountry")?.let { arr -> List(arr.length()) { i -> arr.optString(i) } },
        filterYear = json.optJSONArray("filterYear")?.let { arr -> List(arr.length()) { i -> arr.optString(i) } },
        filterType = json.optJSONArray("filterType")?.let { arr -> List(arr.length()) { i -> arr.optString(i) } },
        sortField = json.optString("sortField"),
        sortType = json.optString("sortType"),
        pagination = json.optJSONObject("pagination")?.let { parsePagination(it) }
    )
}
