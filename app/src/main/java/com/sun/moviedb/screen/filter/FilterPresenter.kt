package com.sun.moviedb.screen.filter

import com.sun.moviedb.utils.base.BasePresenter

interface FilterPresenter : BasePresenter<FilterView> {
    fun loadCategories()
    fun loadCountries()
    fun loadYears()
    fun loadLanguages()
    fun applyFilter(
        typeList: String,
        sortLang: String?,
        country: String?,
        year: String?,
        page: Int = 1
    )
    fun clearFilters()
    fun loadMoreResults()
    val currentPage: Int
    val hasMoreData: Boolean
}
