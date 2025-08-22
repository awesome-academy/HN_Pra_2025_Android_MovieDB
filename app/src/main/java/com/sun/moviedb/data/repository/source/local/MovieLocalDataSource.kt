package com.sun.moviedb.data.repository.source.local

import android.os.Handler
import android.os.Looper
import com.sun.moviedb.data.model.Movie
import com.sun.moviedb.data.repository.source.MovieDataSource
import com.sun.moviedb.data.repository.source.local.dao.FavoriteMovieDao
import com.sun.moviedb.data.repository.source.local.dao.SearchHistoryDao
import com.sun.moviedb.data.repository.source.local.entity.FavoriteMovieEntity
import com.sun.moviedb.data.repository.source.local.entity.SearchHistoryEntity
import java.util.concurrent.Executors

class MovieLocalDataSource private constructor(
    private val favoriteMovieDao: FavoriteMovieDao,
    private val searchHistoryDao: SearchHistoryDao
) : MovieDataSource.Local {
    companion object {
        @Volatile
        private var INSTANCE: MovieLocalDataSource? = null
        fun getInstance(appDatabase: AppDatabase): MovieLocalDataSource {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MovieLocalDataSource(
                    appDatabase.favoriteMovieDao(),
                    appDatabase.searchHistoryDao()
                ).also {
                    INSTANCE = it
                }
            }
        }
    }

    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun insertFavorite(movie: Movie) {
        executor.execute {
            favoriteMovieDao.insertFavorite(FavoriteMovieEntity.fromMovie(movie))
        }
    }

    override fun deleteFavorite(movieId: String) {
        executor.execute {
            val entity = favoriteMovieDao.getFavoriteById(movieId)
            if (entity != null) favoriteMovieDao.deleteFavorite(entity)
        }
    }

    override fun getFavorites(callback: (List<Movie>) -> Unit) {
        executor.execute {
            val result = favoriteMovieDao.getFavorites().map { it.toMovie() }
            mainHandler.post { callback(result) }
        }
    }

    override fun getFavoriteById(movieId: String, callback: (Movie?) -> Unit) {
        executor.execute {
            try {
                val result = favoriteMovieDao.getFavoriteById(movieId)?.toMovie()
                mainHandler.post { callback(result) }
            } catch (e: Exception) {
                mainHandler.post { callback(null) }
            }
        }
    }

    override fun clearFavorites() {
        executor.execute {
            favoriteMovieDao.clearFavorites()
        }
    }

    override fun insertSearchHistory(keyword: String) {
        executor.execute {
            searchHistoryDao.insertSearchHistory(
                SearchHistoryEntity(
                    keyword = keyword
                )
            )
            val allHistory = searchHistoryDao.getSearchHistory()
            if (allHistory.size > 10) {
                allHistory.drop(10).forEach { searchHistoryDao.deleteKeyword(it.keyword) }
            }
        }
    }

    override fun getSearchHistory(callback: (List<String>) -> Unit) {
        executor.execute {
            val result = searchHistoryDao.getSearchHistory().map { it.keyword }
            mainHandler.post { callback(result) }
        }
    }

    override fun clearSearchHistory() {
        executor.execute {
            searchHistoryDao.clearSearchHistory()
        }
    }
}
