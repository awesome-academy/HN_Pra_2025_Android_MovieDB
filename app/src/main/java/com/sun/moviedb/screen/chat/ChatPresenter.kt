package com.sun.moviedb.screen.chat

import com.sun.moviedb.data.model.MessageModel

class ChatPresenter : ChatContract.Presenter{
    private var view: ChatContract.View? = null

    override fun receiveMessages(roomId: String) {
        TODO("Not yet implemented")
    }

    override fun sendMessage(
        roomId: String,
        message: MessageModel
    ) {
        TODO("Not yet implemented")
    }

    override fun attachView(view: ChatContract.View) {
        this.view = view

    }

    override fun detachView() {
        view = null
    }
}

