package com.sun.moviedb.screen.filter

import com.sun.moviedb.data.model.Item
import com.sun.moviedb.data.repository.source.MovieRepository
import com.sun.moviedb.data.repository.source.remote.NetworkResult
import com.sun.moviedb.data.repository.source.remote.dto.MovieListResponse

class FilterPresenterImpl(
    private val repository: MovieRepository
) : FilterPresenter {

    private var view: FilterView? = null

    private var _currentPage = 1
    override val currentPage: Int get() = _currentPage

    private var _hasMoreData = true
    override val hasMoreData: Boolean get() = _hasMoreData

    private var currentMovies = mutableListOf<Item>()
    private var currentFilter: FilterCriteria? = null

    override fun attachView(view: FilterView) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadCategories() {
        repository.getCategories { result ->
            when (result) {
                is NetworkResult.OnSuccess -> {
                    view?.showCategories(result.data)
                }

                is NetworkResult.OnError -> {
                    view?.showError(result.message)
                }
            }
        }
    }

    override fun loadCountries() {
        repository.getCountries { result ->
            when (result) {
                is NetworkResult.OnSuccess -> {
                    view?.showCountries(result.data)
                }

                is NetworkResult.OnError -> {
                    view?.showError(result.message)
                }
            }
        }
    }

    override fun loadYears() {
        val years = (1980..2025).reversed().map { it.toString() }
        view?.showYears(years)
    }

    override fun loadLanguages() {
        val languages = listOf("vietsub", "thuyet-minh", "long-tieng")
        view?.showLanguages(languages)
    }

    override fun applyFilter(
        typeList: String, sortLang: String?, country: String?, year: String?, page: Int
    ) {
        val filterCriteria = FilterCriteria(typeList, sortLang, country, year)

        if (page == 1) {
            currentMovies.clear()
            currentFilter = filterCriteria
            _currentPage = 1
            view?.showLoading(true)
        }

        repository.getFilterMovie(
            typeList = typeList,
            page = page,
            limit = 20,
            sortField = "time",
            sortType = "desc",
            sortLang = sortLang,
            country = country,
            year = year
        ) { result ->
            view?.showLoading(false)
            when (result) {
                is NetworkResult.OnSuccess -> {
                    handleFilterResult(result.data, page)
                }

                is NetworkResult.OnError -> {
                    view?.showError(result.message)
                }
            }
        }
    }

    private fun handleFilterResult(response: MovieListResponse, page: Int) {
        val newMovies = response.items

        if (page == 1) {
            currentMovies.clear()
        }

        currentMovies.addAll(newMovies)

        _hasMoreData = newMovies.size >= 20
        _currentPage = page

        view?.showFilterResults(
            currentMovies.toList(), response.pagination.currentPage, response.pagination.totalItems
        )
        view?.updateResultCount(currentMovies.size)

        if (currentMovies.isEmpty()) {
            view?.showEmptyResult()
        }
    }

    override fun clearFilters() {
        currentMovies.clear()
        currentFilter = null
        _currentPage = 1
        _hasMoreData = true
        view?.clearFilters()
        view?.updateResultCount(0)
    }

    override fun loadMoreResults() {
        if (!hasMoreData) return

        currentFilter?.let { filter ->
            applyFilter(
                typeList = filter.typeList,
                sortLang = filter.sortLang,
                country = filter.country,
                year = filter.year,
                page = currentPage + 1
            )
        }
    }

    private data class FilterCriteria(
        val typeList: String, val sortLang: String?, val country: String?, val year: String?
    )
}
