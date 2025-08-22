package com.sun.moviedb.data.repository.source.firebase.entity

import com.sun.moviedb.data.model.Category
import com.sun.moviedb.data.model.Country
import com.sun.moviedb.data.model.Movie

data class MovieFirebaseEntity(
    val id: String = "",
    val name: String = "",
    val originName: String = "",
    val posterUrl: String = "",
    val thumbUrl: String = "",
    val year: Int = 0,
    val slug: String = "",
    val type: String? = null,
    val quality: String? = null,
    val time: String? = null,
    val category: String? = null,
    val country: String? = null
) {
    fun toMovie(): Movie =
        Movie(
            id = this.id,
            name = this.name,
            originName = this.originName,
            slug = this.slug,
            posterUrl = this.posterUrl,
            thumbUrl = this.thumbUrl,
            year = this.year,
            type = this.type ?: "",
            quality = this.quality ?: "",
            time = this.time ?: "",
            category = if (this.category != null) listOf(Category(name = this.category)) else emptyList(),
            country = if (this.country != null) listOf(Country(name = this.country)) else emptyList()
        )

    companion object {
        fun fromMovie(movie: Movie): MovieFirebaseEntity =
            MovieFirebaseEntity(
                id = movie.id,
                name = movie.name,
                originName = movie.originName,
                posterUrl = movie.posterUrl,
                thumbUrl = movie.thumbUrl,
                year = movie.year,
                slug = movie.slug,
                type = movie.type,
                quality = movie.quality,
                time = movie.time,
                category = movie.category.firstOrNull()?.name,
                country = movie.country.firstOrNull()?.name
            )
    }
}
