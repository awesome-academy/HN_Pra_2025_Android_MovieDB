package com.sun.moviedb.data.repository.auth

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    fun getGoogleSignInClientIntent(): Intent
    suspend fun handleGoogleSignInResult(account: GoogleSignInAccount?): Result<FirebaseUser>
    suspend fun signOut(): Result<Unit>
    fun getCurrentUser(): FirebaseUser?
}