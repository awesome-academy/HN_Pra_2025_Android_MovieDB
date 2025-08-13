package com.sun.moviedb.data.repository.source.remote.parse

import com.sun.moviedb.data.model.Country

fun String.toCountryList(): List<Country> {
    val arr = org.json.JSONArray(this)
    return (0 until arr.length()).map { i ->
        arr.getJSONObject(i).toCountry()
    }
}
