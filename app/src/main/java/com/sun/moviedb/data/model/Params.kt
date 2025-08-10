package com.sun.moviedb.data.model

data class Params(
    val filterCategory: List<String> = emptyList(),
    val filterCountry: List<String> = emptyList(),
    val filterType: List<String> = emptyList(),
    val filterYear: List<String> = emptyList(),
    val pagination: Pagination = Pagination(),
    val slug: String = "",
    val sortField: String = "",
    val sortType: String = "",
    val type_slug: String = ""
)
