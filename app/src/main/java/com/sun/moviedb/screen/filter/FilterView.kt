package com.sun.moviedb.screen.filter

import com.sun.moviedb.data.model.Category
import com.sun.moviedb.data.model.Country
import com.sun.moviedb.data.model.Item
import com.sun.moviedb.utils.base.BaseView

interface FilterView : BaseView {
    fun showFilterResults(movies: List<Item>, currentPage: Int, totalPages: Int)
    fun showCategories(categories: List<Category>)
    fun showCountries(countries: List<Country>)
    fun showYears(years: List<String>)
    fun showLanguages(languages: List<String>)
    fun updateResultCount(count: Int)
    fun clearFilters()
    fun showEmptyResult()
}
