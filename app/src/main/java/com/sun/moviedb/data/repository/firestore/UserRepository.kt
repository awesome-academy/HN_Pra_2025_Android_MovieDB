package com.sun.moviedb.data.repository.firestore

import com.sun.moviedb.data.model.User

interface UserRepository {
    suspend fun searchUsers(query: String): Result<List<User>>
    suspend fun getAllUsers(): Result<List<User>>
}