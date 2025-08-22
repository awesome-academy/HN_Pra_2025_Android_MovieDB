package com.sun.moviedb.data.repository.source.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.sun.moviedb.data.repository.source.MovieDataSource
import com.sun.moviedb.data.repository.source.firebase.entity.MovieFirebaseEntity

class MovieFirebaseDataSource private constructor(
    private val db: FirebaseFirestore
) : MovieDataSource.Firebase {
    companion object {
        @Volatile
        private var instance: MovieFirebaseDataSource? = null
        fun getInstance(db: FirebaseFirestore = FirebaseFirestore.getInstance()): MovieFirebaseDataSource =
            instance ?: synchronized(this) {
                instance ?: MovieFirebaseDataSource(db).also { instance = it }
            }
    }

    override fun addFavoriteMovieToFirebase(
        userId: String,
        movie: MovieFirebaseEntity,
        onComplete: (Boolean) -> Unit
    ) =
        addFavoriteMovie(db, userId, movie, onComplete)

    override fun removeFavoriteMovieFromFirebase(
        userId: String,
        movieId: String,
        onComplete: (Boolean) -> Unit
    ) =
        removeFavoriteMovie(db, userId, movieId, onComplete)

    override fun getFavoriteMoviesFromFirebase(
        userId: String,
        onResult: (List<MovieFirebaseEntity>) -> Unit
    ) =
        getFavoriteMovies(db, userId, onResult)

    override fun addSearchKeywordToFirebase(
        userId: String,
        keyword: String,
        onComplete: (Boolean) -> Unit
    ) =
        addSearchKeyword(db, userId, keyword, onComplete)

    override fun getRecentSearchKeywordsFromFirebase(
        userId: String,
        limit: Long,
        onResult: (List<String>) -> Unit
    ) =
        getRecentSearchKeywords(db, userId, limit, onResult)
}
