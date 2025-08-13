package com.sun.moviedb.screen.detail

import com.sun.moviedb.data.repository.source.MovieRepository
import com.sun.moviedb.data.repository.source.remote.NetworkResult
import com.sun.moviedb.data.repository.source.remote.dto.MovieDetailResponse

class MovieDetailPresenter
    internal constructor(private val mMovieRepository: MovieRepository?)
    : MovieDetailContract.Presenter{
        private var mView: MovieDetailContract.View? = null

    override fun attachView(view: MovieDetailContract.View) {
        this.mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun getDetail(slug: String) {
        mMovieRepository?.getDetailMovie(slug){result ->
            when(result){
                is NetworkResult.OnSuccess<MovieDetailResponse> ->{
                    val movie = result.data.movie
                    val episodes = result.data.episodes

                    mView?.onGetDetailSuccess(movie, episodes)
                }
                is NetworkResult.OnError -> {
                    //todo
                }
            }
        }
    }
}