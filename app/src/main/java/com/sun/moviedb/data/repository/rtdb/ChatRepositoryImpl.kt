package com.sun.moviedb.data.repository.rtdb

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.sun.moviedb.data.model.MessageModel
import com.sun.moviedb.data.repository.source.remote.NetworkResult

class ChatRepositoryImpl : ChatRepository{
    private val chatRef = Firebase.database.reference.child("roomMessages")
    private var messageListener: ChildEventListener? = null // Add this
    private val TAG = "ChatRepositoryImpl"

    override fun sendMessage(
        roomId: String,
        message: MessageModel,
        callback: (NetworkResult<MessageModel>) -> Unit
    ) {
        val messageKey = chatRef.child(roomId).push().key

        if (!messageKey.isNullOrEmpty()) {
            message.id = messageKey
            chatRef.child(roomId).child(messageKey).setValue(message)
                .addOnSuccessListener {
                    Log.d(TAG, "Message sent successfully: $message")
                }
                .addOnFailureListener { error ->
                    callback(NetworkResult.OnError(null, error.message ?: "Failed to send message"))
                    Log.e(TAG, "Failed to send message: ${error.message}")
                }
        }
    }

    override fun receiveMessages(
        roomId: String,
        onResult: (NetworkResult<MessageModel>) -> Unit
    ) {
        messageListener = object : ChildEventListener {
            override fun onChildAdded(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                try {
                    snapshot.getValue<MessageModel>()?.let { item ->
                        onResult(NetworkResult.OnSuccess(item))
                        Log.d(TAG, "New message received: $item")
                    }

                } catch (e: Exception) {
                    onResult(NetworkResult.OnError(9998, e.message ?: "Error to load all messages"))
                    Log.e(TAG, "Error receiving messages: ${e.message}")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        }

        chatRef.child(roomId).orderByChild("createAt").addChildEventListener(messageListener!!)
    }

    override fun removeListener(roomId: String) {
        messageListener?.let {
            chatRef.child(roomId).removeEventListener(it)
            messageListener = null
        }
    }

    override fun deleteChatNode(
        roomId: String,
        callback: (NetworkResult<Unit>) -> Unit
    ) {
        chatRef.child(roomId).removeValue{
            error, _ ->
            if (error != null) {
                callback(NetworkResult.OnError(null, error.message ?: "Failed to delete chat node"))
                Log.e(TAG, "Failed to delete chat node: ${error.message}")
            } else {
                callback(NetworkResult.OnSuccess(Unit))
                Log.d(TAG, "Chat node deleted successfully")
            }
        }
    }

    companion object{
        private const val TAG = "ChatRepositoryImpl"
        private var instance: ChatRepositoryImpl? = null
        fun getInstance(): ChatRepositoryImpl {
            if (instance == null) {
                instance = ChatRepositoryImpl()
            }
            return instance!!
        }
    }
}