package com.sun.moviedb.screen.home

import com.sun.moviedb.utils.base.BasePresenter

interface HomePresenter : BasePresenter<HomeView> {
    fun loadNewestMovies()
    fun loadSeriesMovies(series : String? = null, page : Int = 1)
    val seriesList: List<String>
    fun selectSeries(series: String)
    fun selectPage(page: Int)
}
