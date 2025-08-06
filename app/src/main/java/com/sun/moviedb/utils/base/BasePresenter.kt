package com.sun.moviedb.utils.base

interface BasePresenter<V : BaseView> {
    fun attachView(view: V)
    fun detachView()
}
