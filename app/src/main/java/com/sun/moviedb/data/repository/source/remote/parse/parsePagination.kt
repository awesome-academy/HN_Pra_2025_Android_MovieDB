package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.Pagination
import org.json.JSONObject

fun parsePagination(json: JSONObject): Pagination {
    return Pagination(
        totalItems = json.optInt("totalItems"),
        totalItemsPerPage = json.optInt("totalItemsPerPage"),
        currentPage = json.optInt("currentPage"),
        totalPages = json.optInt("totalPages"),
        updateToday = if (json.has("updateToday")) json.optInt("updateToday") else null
    )
}

