package com.sun.moviedb.data.model

data class User(
    val id: String,
    val username: String,
    val profileImageUrl: String? = null,
    var isSelected: Boolean = false
)
