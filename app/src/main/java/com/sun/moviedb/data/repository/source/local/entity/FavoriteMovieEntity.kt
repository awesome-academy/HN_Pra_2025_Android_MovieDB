package com.sun.moviedb.data.repository.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sun.moviedb.data.model.Category
import com.sun.moviedb.data.model.Country
import com.sun.moviedb.data.model.Movie

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey val id: String,
    val name: String,
    val originName: String,
    val slug: String,
    val posterUrl: String?,
    val thumbUrl: String?,
    val year: Int?,
    val type: String?,
    val quality: String?,
    val time: String?,
    val category: String?,
    val country: String?
) {
    companion object {
        fun fromMovie(movie: Movie): FavoriteMovieEntity {
            return FavoriteMovieEntity(
                id = movie.id,
                name = movie.name,
                originName = movie.originName,
                slug = movie.slug,
                posterUrl = movie.posterUrl,
                thumbUrl = movie.thumbUrl,
                year = movie.year,
                type = movie.type,
                quality = movie.quality,
                time = movie.time,
                category = movie.category.firstOrNull()?.name,
                country = movie.country.firstOrNull()?.name
            )
        }
    }

    fun toMovie(): Movie {
        return Movie(
            id = this.id,
            name = this.name,
            originName = this.originName,
            slug = this.slug,
            posterUrl = this.posterUrl ?: "",
            thumbUrl = this.thumbUrl ?: "",
            year = this.year ?: 0,
            type = this.type ?: "",
            quality = this.quality ?: "",
            time = this.time ?: "",
            category = if (this.category != null) listOf(Category(name = this.category)) else emptyList(),
            country = if (this.country != null) listOf(Country(name = this.country)) else emptyList()
        )
    }
}

