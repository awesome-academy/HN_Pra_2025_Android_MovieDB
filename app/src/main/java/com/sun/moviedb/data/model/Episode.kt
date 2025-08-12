package com.sun.moviedb.data.model

data class Episode(
    val serverData: List<ServerData> = emptyList(),
    val serverName: String = ""
)
