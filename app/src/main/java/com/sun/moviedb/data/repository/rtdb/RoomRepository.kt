package com.sun.moviedb.data.repository.rtdb

import com.sun.moviedb.data.model.Member
import com.sun.moviedb.data.model.Room
import com.sun.moviedb.data.repository.source.remote.NetworkResult

interface RoomRepository {
    fun addRoom(room: Room, onResult: (NetworkResult<Unit>) -> Unit)
    fun removeRoom(roomId: String)
    fun getRoom(roomId: String, onResult: (NetworkResult<Room>) -> Unit)
    fun changeSetting()
    fun changeHost(roomId: String, newHostId: String, onResult: (NetworkResult<Unit>) -> Unit)
    fun getCommand(roomId: String, onResult: (NetworkResult<String>) -> Unit)
    fun removeListener(roomId: String)
    fun deleteRoomNode(roomId: String, onResult: (NetworkResult<Unit>) -> Unit)
}

