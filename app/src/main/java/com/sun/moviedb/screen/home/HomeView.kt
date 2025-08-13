package com.sun.moviedb.screen.home

import com.sun.moviedb.data.model.Item
import com.sun.moviedb.utils.base.BaseView

interface HomeView : BaseView {
    fun showNewestMoviesLoading(isLoading: Boolean)
    fun showSeriesMoviesLoading(isLoading: Boolean)
    fun showNewestMovies(items: List<Item>)
    fun showSeriesMovies(items: List<Item>, currentPage: Int, totalPage: Int)
}
