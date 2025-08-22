package com.sun.moviedb.data.repository.source

import com.sun.moviedb.data.model.Category
import com.sun.moviedb.data.model.Country
import com.sun.moviedb.data.model.Movie
import com.sun.moviedb.data.repository.source.firebase.MovieFirebaseDataSource
import com.sun.moviedb.data.repository.source.firebase.entity.MovieFirebaseEntity
import com.sun.moviedb.data.repository.source.remote.NetworkResult
import com.sun.moviedb.data.repository.source.remote.dto.MovieDetailResponse
import com.sun.moviedb.data.repository.source.remote.dto.MovieListResponse
import java.util.concurrent.Future

class MovieRepository(
    private val remote: MovieDataSource.Remote,
    private val local: MovieDataSource.Local,
    private val firebase: MovieFirebaseDataSource
) : MovieDataSource.Local, MovieDataSource.Remote, MovieDataSource.Firebase {

    /* *
    * LOCAL
    * */
    override fun insertFavorite(movie: Movie) {
        local.insertFavorite(movie)
    }

    override fun deleteFavorite(movieId: String) {
        local.deleteFavorite(movieId)
    }

    override fun getFavorites(
        callback: (List<Movie>) -> Unit
    ) {
        local.getFavorites(callback)
    }

    override fun getFavoriteById(
        movieId: String,
        callback: (Movie?) -> Unit
    ) {
        local.getFavoriteById(movieId, callback)
    }

    override fun clearFavorites() {
        local.clearFavorites()
    }

    override fun insertSearchHistory(keyword: String) {
        local.insertSearchHistory(keyword)
    }

    override fun getSearchHistory(callback: (List<String>) -> Unit) {
        local.getSearchHistory(callback)
    }

    override fun clearSearchHistory() {
        local.clearSearchHistory()
    }

    /* *
    * REMOTE
    * */
    override fun getDetailMovie(
        slug: String,
        callback: (NetworkResult<MovieDetailResponse>) -> Unit
    ): Future<*> = remote.getDetailMovie(slug, callback)

    override fun getNewestMovie(
        page: Int,
        callback: (NetworkResult<MovieListResponse>) -> Unit
    ): Future<*> = remote.getNewestMovie(page, callback)

    override fun getSeriesMovie(
        typeList: String,
        page: Int,
        limit: Int,
        callback: (NetworkResult<MovieListResponse>) -> Unit
    ): Future<*> = remote.getSeriesMovie(typeList, page, limit, callback)

    override fun getFilterMovie(
        typeList: String,
        page: Int,
        limit: Int,
        sortField: String,
        sortType: String,
        sortLang: String?,
        country: String?,
        year: String?,
        callback: (NetworkResult<MovieListResponse>) -> Unit
    ): Future<*> = remote.getFilterMovie(
        typeList, page, limit, sortField, sortType, sortLang, country, year, callback
    )

    override fun searchMovie(
        keyword: String,
        page: Int,
        limit: Int,
        sortField: String,
        sortType: String,
        sortLang: String?,
        category: String?,
        country: String?,
        year: String?,
        callback: (NetworkResult<MovieListResponse>) -> Unit
    ): Future<*> = remote.searchMovie(
        keyword,
        page,
        limit,
        sortField,
        sortType,
        sortLang,
        category,
        country,
        year,
        callback
    )

    override fun getCategories(callback: (NetworkResult<List<Category>>) -> Unit): Future<*> =
        remote.getCategories(callback)

    override fun getCountries(callback: (NetworkResult<List<Country>>) -> Unit): Future<*> =
        remote.getCountries(callback)

    /* *
     * FIREBASE
     * */
    override fun addFavoriteMovieToFirebase(
        userId: String,
        movie: MovieFirebaseEntity,
        onComplete: (Boolean) -> Unit
    ) {
        firebase.addFavoriteMovieToFirebase(userId, movie, onComplete)
    }

    override fun removeFavoriteMovieFromFirebase(
        userId: String,
        movieId: String,
        onComplete: (Boolean) -> Unit
    ) {
        firebase.removeFavoriteMovieFromFirebase(userId, movieId, onComplete)
    }

    override fun getFavoriteMoviesFromFirebase(
        userId: String,
        onResult: (List<MovieFirebaseEntity>) -> Unit
    ) {
        firebase.getFavoriteMoviesFromFirebase(userId, onResult)
    }

    override fun addSearchKeywordToFirebase(
        userId: String,
        keyword: String,
        onComplete: (Boolean) -> Unit
    ) {
        firebase.addSearchKeywordToFirebase(userId, keyword, onComplete)
    }

    override fun getRecentSearchKeywordsFromFirebase(
        userId: String,
        limit: Long,
        onResult: (List<String>) -> Unit
    ) {
        firebase.getRecentSearchKeywordsFromFirebase(userId, limit, onResult)
    }


    fun syncFavoritesFromFirebase(userId: String, onComplete: (Boolean) -> Unit) {
        getFavoriteMoviesFromFirebase(userId) { firebaseMovies ->
            try {
                clearFavorites()
                firebaseMovies.forEach { entity ->
                    insertFavorite(entity.toMovie())
                }
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }

    fun backupSearchHistoryToFirebase(userId: String, onComplete: (Boolean) -> Unit) {
        getSearchHistory { localKeywords ->
            if (localKeywords.isEmpty()) {
                onComplete(true)
                return@getSearchHistory
            }
            var completed = 0
            var failed = false
            localKeywords.forEach { keyword ->
                addSearchKeywordToFirebase(userId, keyword) { success ->
                    if (!success) failed = true
                    completed++
                    if (completed == localKeywords.size) {
                        if (!failed) clearSearchHistory()
                        onComplete(!failed)
                    }
                }
            }
        }
    }

    fun clearLocalData() {
        clearFavorites()
        clearSearchHistory()
    }

}
