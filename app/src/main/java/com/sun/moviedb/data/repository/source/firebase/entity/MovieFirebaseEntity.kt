package com.sun.moviedb.data.repository.source.firebase.entity

import com.sun.moviedb.data.model.Movie

data class MovieFirebaseEntity(
    val id: String = "",
    val name: String = "",
    val originName: String = "",
    val posterUrl: String = "",
    val thumbUrl: String = "",
    val year: Int = 0,
    val slug: String = ""
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
            // The rest are left as default/empty
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
                slug = movie.slug
            )
    }
}
