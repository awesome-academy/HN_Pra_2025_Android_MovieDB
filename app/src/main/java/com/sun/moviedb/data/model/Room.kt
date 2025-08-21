package com.sun.moviedb.data.model

data class Room(
    val roomId: String = "", // Host UID | Room : User = 1:1
    val roomName: String = "", // Name of Movie
    val roomCode: String = "", // Movie slug
    val createAt: Long = 0L,
    val createBY : String= "", // Host UID
    val command: String= "",
    val settings: RoomSettings = RoomSettings()

)

data class RoomSettings(
    val maxMember: Int = 10,
    val isAllowChat: Boolean = true
)

