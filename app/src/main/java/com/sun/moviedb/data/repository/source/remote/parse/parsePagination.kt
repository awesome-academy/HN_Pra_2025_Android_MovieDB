package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.repository.source.remote.model.Pagination
import org.json.JSONObject

fun parsePagination(json: JSONObject): Pagination {
    return Pagination(
        totalItems = if (json.has("totalItems")) json.optInt("totalItems") else null,
        totalItemsPerPage = if (json.has("totalItemsPerPage")) json.optInt("totalItemsPerPage") else null,
        currentPage = if (json.has("currentPage")) json.optInt("currentPage") else null,
        totalPages = if (json.has("totalPages")) json.optInt("totalPages") else null,
        updateToday = if (json.has("updateToday")) json.optInt("updateToday") else null
    )
}
