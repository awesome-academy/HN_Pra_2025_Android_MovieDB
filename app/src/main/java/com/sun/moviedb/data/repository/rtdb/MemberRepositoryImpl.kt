package com.sun.moviedb.data.repository.rtdb

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database
import com.sun.moviedb.data.model.Member
import com.sun.moviedb.data.repository.source.remote.NetworkResult

class MemberRepositoryImpl : MemberRepository {
    private val memberRef = Firebase.database.reference.child("members")
    private var memberListener: ChildEventListener? = null

    private val TAG = "MemberRepositoryImpl"

    override fun addMember(
        roomId: String,
        member: Member,
        onResult: (NetworkResult<Unit>) -> Unit
    ) {
        memberRef.child(roomId).setValue(member)
            .addOnSuccessListener {
                Log.d(TAG, "Member added successfully: $member")
                onResult(NetworkResult.OnSuccess(Unit))
            }
            .addOnFailureListener { error ->
                onResult(NetworkResult.OnError(null, error.message ?: "Failed to add member"))
                Log.e(TAG, "Failed to add member: ${error.message}")
            }
    }

    override fun removeMember(
        roomId: String,
        memberId: String,
        onResult: (NetworkResult<Unit>) -> Unit
    ) {
        memberRef.child(roomId).child(memberId).removeValue()
            .addOnSuccessListener {
                Log.d(TAG, "Member removed successfully: $memberId")
                onResult(NetworkResult.OnSuccess(Unit))
            }
            .addOnFailureListener { error ->
                onResult(NetworkResult.OnError(null, error.message ?: "Failed to remove member"))
                Log.e(TAG, "Failed to remove member: ${error.message}")
            }
    }

    override fun getMembers(
        roomId: String,
        onResult: (NetworkResult<List<Member>>) -> Unit
    ) {
        memberListener = object: ChildEventListener{
            override fun onChildAdded(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
    }

    override fun deleteMemberNode(
        roomId: String,
        memberId: String,
        onResult: (NetworkResult<Unit>) -> Unit
    ) {
        memberRef.child(roomId).removeValue()
            .addOnSuccessListener {
                Log.d(TAG, "Member node deleted successfully: $memberId")
                onResult(NetworkResult.OnSuccess(Unit))
            }
            .addOnFailureListener {
                onResult(NetworkResult.OnError(null, it.message ?: "Failed to delete member node"))
                Log.e(TAG, "Failed to delete member node: ${it.message}")
            }
    }

    override fun removeListener(roomId: String) {
        memberListener?.let {
            memberRef.child(roomId).removeEventListener(it)
            memberListener = null
        }
    }


    companion object{
        private var instance: MemberRepositoryImpl? = null

        fun getInstance(): MemberRepositoryImpl {
            if (instance == null) {
                instance = MemberRepositoryImpl()
            }
            return instance!!
        }
    }
}