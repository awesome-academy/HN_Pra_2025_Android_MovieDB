package com.sun.moviedb.screen.search

import com.sun.moviedb.data.model.Item
import com.sun.moviedb.utils.base.BaseView

interface SearchView : BaseView {
    fun showSearchResults(items: List<Item>)
    fun showLanguages(languages: List<String>)
    fun showEmptyResult()
    fun showLoadMoreError()
}
