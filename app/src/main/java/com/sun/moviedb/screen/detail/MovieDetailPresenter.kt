package com.sun.moviedb.screen.detail

import com.sun.moviedb.data.model.Movie
import com.sun.moviedb.data.repository.source.MovieRepository
import com.sun.moviedb.data.repository.source.firebase.entity.MovieFirebaseEntity
import com.sun.moviedb.data.repository.source.remote.NetworkResult
import com.sun.moviedb.data.repository.source.remote.dto.MovieDetailResponse

class MovieDetailPresenter
internal constructor(private val mMovieRepository: MovieRepository?) :
    MovieDetailContract.Presenter {
    private var mView: MovieDetailContract.View? = null

    override fun attachView(view: MovieDetailContract.View) {
        this.mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun getDetail(slug: String) {
        mMovieRepository?.getDetailMovie(slug) { result ->
            when (result) {
                is NetworkResult.OnSuccess<MovieDetailResponse> -> {
                    val movie = result.data.movie
                    val episodes = result.data.episodes

                    mView?.onGetDetailSuccess(movie, episodes)
                }

                is NetworkResult.OnError -> {
                    val errorMessage = result.message
                    mView?.showError(errorMessage)
                }
            }
        }
    }

    override fun checkFavorite(movieId: String, userId: String) {
        mMovieRepository?.getFavoriteById(movieId) { movie ->
            mView?.onCheckFavorite(movie != null)
        }
    }

    override fun onFavClicked(movie: Movie, isFavourite: Boolean, userId: String) {
        if (isFavourite) {
            mMovieRepository?.deleteFavorite(movie.id)
            checkFavorite(movie.id, userId)
            mMovieRepository?.removeFavoriteMovieFromFirebase(userId, movie.id) { isSuccess ->
                mView?.onMovieToFirebaseSuccess("Removed from Firebase ${if (isSuccess) "successfully" else "failed"}")
            }

        } else {
            mMovieRepository?.insertFavorite(movie)
            checkFavorite(movie.id, userId)
            mMovieRepository?.addFavoriteMovieToFirebase(
                userId,
                MovieFirebaseEntity.fromMovie(movie)
            ) { isSuccess ->
                mView?.onMovieToFirebaseSuccess("Added to Firebase ${if (isSuccess) "successfully" else "failed"}")
            }
        }
    }
}
