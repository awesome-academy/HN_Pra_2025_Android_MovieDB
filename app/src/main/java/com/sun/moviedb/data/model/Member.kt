package com.sun.moviedb.data.model

data class Member(
    var memberId: String = "",
    val memberName: String = "",
    var linkAvatar: String = "",
    var joinAt: Long = 0L,
    var isHost: Boolean = false,
)


