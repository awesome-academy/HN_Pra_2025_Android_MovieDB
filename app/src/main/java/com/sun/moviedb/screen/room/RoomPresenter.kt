package com.sun.moviedb.screen.room

class RoomPresenter : RoomContract.Presenter {
    private var view: RoomContract.View? = null

    override fun attachView(view: RoomContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null

    }
}