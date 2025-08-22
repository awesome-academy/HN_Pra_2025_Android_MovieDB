package com.sun.moviedb.utils.session

object RoomSession {
    var roomId: String? = null

    fun updateRoomId(newRoomId: String) {
        roomId = newRoomId
    }
}

