package com.sun.moviedb.screen.search

import com.sun.moviedb.utils.base.BasePresenter

interface SearchPresenter : BasePresenter<SearchView> {
    fun searchMovie(keyword: String, sortLang: String?, page: Int)
    fun loadMoreResults()
    fun clearSearch()
    fun loadLanguages()
}
