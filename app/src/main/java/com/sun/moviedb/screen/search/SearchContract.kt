package com.sun.moviedb.screen.search

import com.sun.moviedb.data.model.Item
import com.sun.moviedb.utils.base.BasePresenter
import com.sun.moviedb.utils.base.BaseView

interface SearchContract {
    interface SearchView : BaseView {
        fun showSearchResults(items: List<Item>)
        fun showLanguages(languages: List<String>)
        fun showEmptyResult()
        fun showLoadMoreError()
    }

    interface SearchPresenter : BasePresenter<SearchView> {
        fun searchMovie(keyword: String, sortLang: String?, page: Int)
        fun loadMoreResults()
        fun loadLanguages()
        fun clearSearch()
    }
}


