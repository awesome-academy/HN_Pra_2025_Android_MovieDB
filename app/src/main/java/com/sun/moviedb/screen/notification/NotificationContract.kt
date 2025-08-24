package com.sun.moviedb.screen.notification

import com.sun.moviedb.utils.base.BasePresenter
import com.sun.moviedb.utils.base.BaseView

interface NotificationContract {
    interface View : BaseView{

    }

    interface Presenter : BasePresenter<View>{

    }
}

