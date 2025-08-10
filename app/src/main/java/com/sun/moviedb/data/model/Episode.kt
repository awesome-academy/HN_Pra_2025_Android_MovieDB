package com.sun.moviedb.data.model

data class Episode(
    val server_data: List<ServerData> = emptyList(),
    val server_name: String = ""
)
