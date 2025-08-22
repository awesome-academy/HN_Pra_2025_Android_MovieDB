package com.sun.moviedb.data.repository.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sun.moviedb.data.repository.source.local.dao.FavoriteMovieDao
import com.sun.moviedb.data.repository.source.local.dao.SearchHistoryDao
import com.sun.moviedb.data.repository.source.local.entity.FavoriteMovieEntity
import com.sun.moviedb.data.repository.source.local.entity.SearchHistoryEntity
import com.sun.moviedb.utils.Constants

@Database(
    entities = [FavoriteMovieEntity::class, SearchHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.MOVIE_DB_NAME
                ).build().also { INSTANCE = it }
            }
        }
    }
}
