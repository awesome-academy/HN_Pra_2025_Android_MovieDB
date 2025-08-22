package com.sun.moviedb.data.repository.source.local.dao

import androidx.room.*
import com.sun.moviedb.data.repository.source.local.entity.FavoriteMovieEntity

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(movie: FavoriteMovieEntity)

    @Delete
    fun deleteFavorite(movie: FavoriteMovieEntity)

    @Query("SELECT * FROM favorite_movies")
    fun getFavorites(): List<FavoriteMovieEntity>

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId LIMIT 1")
    fun getFavoriteById(movieId: String): FavoriteMovieEntity?

    @Query("DELETE FROM favorite_movies")
    fun clearFavorites()
}
