package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.model.Category

fun String.toCategoryList(): List<Category> {
    val arr = org.json.JSONArray(this)
    return (0 until arr.length()).map { i ->
        arr.getJSONObject(i).toCategory()
    }
}
