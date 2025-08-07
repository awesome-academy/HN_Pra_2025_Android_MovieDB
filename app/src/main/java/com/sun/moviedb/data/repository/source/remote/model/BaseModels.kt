package com.sun.moviedb.data.repository.source.remote.model

data class TmdbInfo(
    val type: String?,
    val id: String?,
    val season: Int?,
    val vote_average: Double?,
    val vote_count: Int?
)

data class ImdbInfo(
    val id: String?
)

data class ModifiedInfo(
    val time: String?
)

data class CreatedInfo(
    val time: String?
)

data class Category(
    val id: String?,
    val name: String?,
    val slug: String?
)

data class Country(
    val id: String?,
    val name: String?,
    val slug: String?
)

