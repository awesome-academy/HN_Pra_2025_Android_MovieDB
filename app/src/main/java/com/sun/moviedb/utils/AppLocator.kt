package com.sun.moviedb.utils

import com.sun.moviedb.data.repository.rtdb.MemberRepository
import com.sun.moviedb.data.repository.rtdb.MemberRepositoryImpl
import com.sun.moviedb.data.repository.rtdb.RoomRepository
import com.sun.moviedb.data.repository.rtdb.RoomRepositoryImpl

object AppLocator {
    val roomRepository: RoomRepository by lazy {
        RoomRepositoryImpl.getInstance()
    }

    val memberRepository: MemberRepository by lazy {
        MemberRepositoryImpl.getInstance()
    }
}

