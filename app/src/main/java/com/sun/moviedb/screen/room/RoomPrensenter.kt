package com.sun.moviedb.screen.room

class RoomPrensenter : RoomContract.Presenter {

    private var view: RoomContract.View? = null

    override fun attachView(view: RoomContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

}

