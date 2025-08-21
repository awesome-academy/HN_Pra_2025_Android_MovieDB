package com.sun.moviedb.data.repository.rtdb

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.sun.moviedb.data.model.Room
import com.sun.moviedb.data.repository.source.remote.NetworkResult

class RoomRepositoryImpl : RoomRepository {
    private val roomRef = Firebase.database.reference.child("rooms")
    private var roomListener: ValueEventListener? = null
    private val TAG = "RoomRepositoryImpl"

    override fun addRoom(
        room: Room,
        onResult: (NetworkResult<Unit>) -> Unit
    ) {
        roomRef.child(room.roomId).setValue(room)
            .addOnSuccessListener {
                Log.d(TAG, "Room added successfully: $room")
            }
            .addOnFailureListener { error ->
                onResult(NetworkResult.OnError(null, error.message ?: "Failed to add room"))
                Log.e(TAG, "Failed to add room: ${error.message}")
            }
    }

    override fun removeRoom(roomId: String) {
        if (roomId.isNotEmpty()) {
            roomRef.child(roomId).removeValue { error, _ ->
                if (error != null) {
                    Log.e(TAG, "Failed to remove room: ${error.message}")
                } else {
                    Log.d(TAG, "Room removed successfully: $roomId")
                }
            }
        } else {
            Log.e(TAG, "Room ID is blank, cannot remove room.")

        }
    }

    override fun getRoom(
        roomId: String,
        onResult: (NetworkResult<Room>) -> Unit
    ) {
        if (roomId.isNotEmpty()) {
            roomListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //handle when host change
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
        }

        roomRef.addValueEventListener(roomListener!!)
    }

    override fun changeSetting() {
    }

    override fun changeHost(
        roomId: String,
        newHostId: String,
        onResult: (NetworkResult<Unit>) -> Unit
    ) {
    }

    override fun getCommand(
        roomId: String,
        onResult: (NetworkResult<String>) -> Unit
    ) {
    }

    override fun removeListener(roomId: String) {
        roomListener?.let {
            roomRef.child(roomId).removeEventListener(it)
            roomListener = null
        }
    }

    override fun deleteRoomNode(
        roomId: String,
        onResult: (NetworkResult<Unit>) -> Unit
    ) {
        if (roomId.isNotEmpty()) {
            roomRef.child(roomId).removeValue { error, ref ->
                if (error != null) {
                    onResult(NetworkResult.OnError(null, error.message))
                    Log.e(TAG, "Failed to delete room node: ${error.message}")
                } else {
                    onResult(NetworkResult.OnSuccess(Unit))
                    Log.d(TAG, "Room node deleted successfully")
                }
            }
        }
    }

    companion object {
        private var instance: RoomRepositoryImpl? = null
        fun getInstance(): RoomRepositoryImpl {
            if (instance == null) {
                instance = RoomRepositoryImpl()
            }
            return instance!!
        }
    }
}

