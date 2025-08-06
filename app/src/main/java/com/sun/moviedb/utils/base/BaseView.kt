package com.sun.moviedb.utils.base

interface BaseView {
    fun showLoading(isLoading: Boolean)
    fun showError(message: String)
}
