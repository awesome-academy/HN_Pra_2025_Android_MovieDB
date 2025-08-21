package com.sun.moviedb.data.repository.rtdb

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.sun.moviedb.data.model.Member
import com.sun.moviedb.data.repository.source.remote.NetworkResult

class MemberRepositoryImpl : MemberRepository {
    private val memberRef = Firebase.database.reference.child(membersPath)
    private var memberListener: ChildEventListener? = null
    private val TAG = "MemberRepositoryImpl"

    override fun addMember(
        roomId: String,
        member: Member,
        onResult: (NetworkResult<Unit>) -> Unit
    ) {
        val memberKey = memberRef.child(roomId).push().key!!
        member.memberId = memberKey

        memberRef.child(roomId).child(memberKey).setValue(member)
            .addOnSuccessListener {
                onResult(NetworkResult.OnSuccess(Unit))
                Log.d(TAG, "Member added successfully: $memberKey")
            }
            .addOnFailureListener { error ->
                onResult(NetworkResult.OnError(null, error.message ?: "Cannot add member"))
                Log.e(TAG, "Failed to add member: $memberKey", error)
            }
    }

    override fun removeMember(
        roomId: String,
        memberId: String,
        onResult: (NetworkResult<Unit>) -> Unit
    ) {
        memberRef.child(roomId).child(memberId).removeValue()
            .addOnSuccessListener {
                onResult(NetworkResult.OnSuccess(Unit))
                Log.d(TAG, "Member removed successfully: $memberId")
            }
            .addOnFailureListener { error ->
                onResult(NetworkResult.OnError(null, error.message ?: "Cannot remove member"))
                Log.e(TAG, "Failed to remove member: $memberId", error)
            }
    }

    override fun getMembers(
        roomId: String,
        onResult: (NetworkResult<Member>) -> Unit
    ) {
        memberListener = object : ChildEventListener{
            override fun onChildAdded(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                if (!snapshot.exists()) {
                    onResult(NetworkResult.OnError(null, "No members found"))
                    Log.d(TAG, "No members found for room: $roomId")
                    return
                }

                val member = snapshot.getValue<Member>()
                if (member == null) {
                    onResult(NetworkResult.OnError(null, "Failed to parse member data"))
                    Log.d(TAG, "Failed to parse member data for room: $roomId")
                    return
                }

                onResult(NetworkResult.OnSuccess(member))
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                val member = snapshot.getValue<Member>()
                if (member != null) {
                    onResult(NetworkResult.OnSuccess(member))
                    Log.d(TAG, "Member updated: ${member.memberId}")
                } else {
                    onResult(NetworkResult.OnError(null, "Failed to parse updated member data"))
                    Log.d(TAG, "Failed to parse updated member data for: ${snapshot.key}")
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val memberId = snapshot.key
                if (memberId != null) {
                    onResult(NetworkResult.OnError(null, "Member removed: $memberId"))
                    Log.d(TAG, "Member leaved: $memberId")
                } else {
                    onResult(NetworkResult.OnError(null, "Member ID not found"))
                    Log.d(TAG, "Member ID not found for removal")
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        }

        memberRef.addChildEventListener(memberListener!!)
    }

    override fun deleteMemberNode(
        roomId: String,
        onResult: (NetworkResult<Unit>) -> Unit
    ) {
        memberRef.child(roomId).removeValue()
            .addOnSuccessListener {
                onResult(NetworkResult.OnSuccess(Unit))
                Log.d(TAG, "MemberNode removed successfully: $roomId")
            }
            .addOnFailureListener { error ->
                onResult(NetworkResult.OnError(null, error.message ?: "Cannot remove membernode"))
                Log.e(TAG, "Failed to remove member node: $roomId", error)
            }
    }

    override fun removeListener(roomId: String) {
        memberListener?.let {
            memberRef.child(roomId).removeEventListener(it)
            memberListener = null
        }
    }

    companion object{
        private const val membersPath = "members"

        private var intance: MemberRepositoryImpl? = null
        fun getInstance(): MemberRepositoryImpl {
            if (intance == null) {
                intance = MemberRepositoryImpl()
            }
            return intance!!
        }
    }
}

