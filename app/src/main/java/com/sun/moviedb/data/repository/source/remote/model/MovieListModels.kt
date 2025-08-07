package com.sun.moviedb.data.repository.source.remote.model


data class MovieItem(
    val tmdb: TmdbInfo?,
    val imdb: ImdbInfo?,
    val created: CreatedInfo?,
    val modified: ModifiedInfo?,
    val _id: String?,
    val name: String?,
    val slug: String?,
    val origin_name: String?,
    val type: String?,
    val poster_url: String?,
    val thumb_url: String?,
    val sub_docquyen: Boolean?,
    val chieurap: Boolean? = null,
    val time: String?,
    val episode_current: String?,
    val quality: String?,
    val lang: String?,
    val year: Int?,
    val category: List<Category>?,
    val country: List<Country>?
)

data class MovieResponse(
    val status: Boolean?,
    val msg: String?,
    val items: List<MovieItem>?,
    val pagination: Pagination?
)

data class Pagination(
    val totalItems: Int?,
    val totalItemsPerPage: Int?,
    val currentPage: Int?,
    val totalPages: Int?,
    val updateToday: Int? = null
)

