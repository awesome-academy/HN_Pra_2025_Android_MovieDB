package com.sun.moviedb.screen.login

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.sun.moviedb.data.repository.auth.AuthRepository

class LoginInteractorImpl(
    private val authRepository: AuthRepository
) : LoginContract.Interactor {

    override suspend fun performGoogleLogin(
        account: GoogleSignInAccount?,
        listener: LoginContract.Interactor.OnAuthFinishedListener
    ) {
        if (account == null) {
            listener.onAuthFailure("Google Sign-In account is null.")
            return
        }
        val result = authRepository.handleGoogleSignInResult(account)
        result.fold(
            onSuccess = { firebaseUser ->
                listener.onAuthSuccess(firebaseUser)
            },
            onFailure = { exception ->
                listener.onAuthFailure(exception.localizedMessage ?: "Authentication failed.")
            }
        )
    }
}