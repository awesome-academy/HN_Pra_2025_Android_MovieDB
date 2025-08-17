package com.sun.moviedb.screen.detail

import com.sun.moviedb.data.model.Episode
import com.sun.moviedb.data.model.Movie
import com.sun.moviedb.utils.base.BasePresenter
import com.sun.moviedb.utils.base.BaseView

interface MovieDetailContract {
    interface View : BaseView {
        fun onGetDetailSuccess(movie: Movie, episodes: List<Episode>)
        fun onGetDetailError(message: String)
    }

    interface Presenter : BasePresenter<View> {
        fun getDetail(slug: String)
        fun onFavClicked(movie: Movie, isFavourite: Boolean)
    }
}


