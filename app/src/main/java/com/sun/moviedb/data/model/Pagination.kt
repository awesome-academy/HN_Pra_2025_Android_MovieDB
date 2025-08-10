package com.sun.moviedb.data.model

data class Pagination(
    val currentPage: Int = 1,
    val totalItems: Int = 0,
    val totalItemsPerPage: Int = 20,
    val totalPages: Int = 1
)
