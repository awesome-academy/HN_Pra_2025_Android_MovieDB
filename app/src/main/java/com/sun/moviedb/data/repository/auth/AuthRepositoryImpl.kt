package com.sun.moviedb.data.repository.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.sun.moviedb.R
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(private val context: Context) : AuthRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val googleSignInClient: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    override fun getGoogleSignInClientIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    override suspend fun handleGoogleSignInResult(account: GoogleSignInAccount?): Result<FirebaseUser> {
        return try {
            if (account?.idToken == null) {
                Result.failure(IllegalArgumentException("Google Sign-In account or ID token is null."))
            } else {
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                val authResult = firebaseAuth.signInWithCredential(credential).await()
                if (authResult.user != null) {
                    Result.success(authResult.user!!)
                } else {
                    Result.failure(Exception("Firebase authentication failed: User is null."))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            googleSignInClient.signOut().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}
