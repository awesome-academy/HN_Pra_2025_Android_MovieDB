package com.sun.moviedb.data.model

data class Member(
    val userId: String = "",
    val userName: String = "",
    val linkAvatar: String = "",
    val joinAt: Long = 0L,
    val isHost: Boolean = false,
)