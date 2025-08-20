package com.sun.moviedb.screen.login

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.sun.moviedb.data.repository.auth.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginPresenterImpl(
    private val authRepository: AuthRepository,
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main + Job())
) : LoginContract.Presenter {

    private var view: LoginContract.View? = null
    private val TAG = "LoginPresenter"

    override fun attachView(view: LoginContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun onLoginButtonClicked() {
        view?.showLoading(true)
        val signInIntent = authRepository.getGoogleSignInClientIntent()
        view?.launchGoogleSignInActivity(signInIntent)
    }

    override fun handleGoogleSignInResult(account: GoogleSignInAccount?, exception: Exception?) {
        view?.showLoading(true)
        if (exception != null) {
            Log.w(TAG, "Google Sign In Failed (from Activity)", exception)
            view?.showLoading(false)
            view?.showGoogleSignInFailed(exception.localizedMessage ?: "Google Sign-In failed.")
            return
        }
        if (account == null) {
            Log.w(TAG, "Google Sign In Account is null (from Activity).")
            view?.showLoading(false)
            view?.showGoogleSignInFailed("Received null account from Google Sign-In.")
            return
        }

        mainScope.launch {
            val result = authRepository.handleGoogleSignInResult(account)
            withContext(Dispatchers.Main) {
                view?.showLoading(false)
                result.fold(
                    onSuccess = { firebaseUser ->
                        Log.d(TAG, "Firebase Auth Successful: ${firebaseUser.displayName}")
                        view?.showLoginSuccess(firebaseUser)
                        view?.navigateToMain()
                    },
                    onFailure = { authException ->
                        Log.w(TAG, "Firebase Authentication Failed", authException)
                        view?.showLoginError("Authentication Failed: ${authException.localizedMessage}")
                    }
                )
            }
        }
    }
}
