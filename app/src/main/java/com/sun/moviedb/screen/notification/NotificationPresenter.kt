package com.sun.moviedb.screen.notification

class NotificationPresenter : NotificationContract.Presenter {
    private var view: NotificationContract.View? = null
    override fun attachView(view: NotificationContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

}