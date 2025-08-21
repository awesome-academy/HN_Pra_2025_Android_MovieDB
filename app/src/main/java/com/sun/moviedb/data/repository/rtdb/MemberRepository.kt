package com.sun.moviedb.data.repository.rtdb

import com.sun.moviedb.data.model.Member
import com.sun.moviedb.data.repository.source.remote.NetworkResult

interface MemberRepository {
    fun addMember(roomId: String, member: Member, onResult: (NetworkResult<Unit>) -> Unit)
    fun removeMember(roomId: String, memberId: String, onResult: (NetworkResult<Unit>) -> Unit)
    fun getMembers(roomId: String, onResult: (NetworkResult<List<Member>>) -> Unit)
    fun deleteMemberNode(roomId: String, memberId: String, onResult: (NetworkResult<Unit>) -> Unit)
    fun removeListener(roomId: String)

}