package com.sun.moviedb.screen.searchUser

import android.util.Log
import com.sun.moviedb.data.model.User
import com.sun.moviedb.data.repository.firestore.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchUserPresenterImpl(
    private val userRepository: UserRepository,
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main + Job())
) : SearchUserContract.Presenter {

    private var view: SearchUserContract.View? = null
    private val TAG = "SearchUserPresenter"

    private val currentSearchableUsers = mutableListOf<User>()
    private val currentChosenUsers = mutableListOf<User>()

    override fun attachView(view: SearchUserContract.View) {
        this.view = view
        loadInitialUsers()
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadInitialUsers() {
        view?.showLoading()
        mainScope.launch {
            val result = userRepository.getAllUsers()
            withContext(Dispatchers.Main) {
                view?.hideLoading()
                result.fold(
                    onSuccess = { users ->
                        currentSearchableUsers.clear()
                        currentSearchableUsers.addAll(users.filter { initialUser ->
                            currentChosenUsers.none { chosenUser -> chosenUser.id == initialUser.id }
                        })

                        if (currentSearchableUsers.isEmpty()) {
                            view?.showSearchableUsersEmpty("No users found.")
                        } else {
                            view?.hideSearchableUsersEmpty()
                        }
                        view?.displaySearchableUsers(ArrayList(currentSearchableUsers))
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "Error fetching initial users", exception)
                        view?.displayError("Failed to load users: ${exception.localizedMessage}")
                        view?.showSearchableUsersEmpty("Error loading users. Please try again.")
                    }
                )
            }
        }
    }

    override fun searchUsers(query: String) {
        if (query.isBlank()) {
            loadInitialUsers()
            return
        }
        if (query.length < 2 && query.isNotEmpty()) {
            view?.displaySearchableUsers(emptyList())
            view?.showSearchableUsersEmpty("Enter at least 2 characters to search.")
            return
        }

        view?.showLoading()
        mainScope.launch {
            val result = userRepository.searchUsers(query)
            // ... (rest of searchUsers logic remains the same)
            withContext(Dispatchers.Main) {
                view?.hideLoading()
                result.fold(
                    onSuccess = { users ->
                        currentSearchableUsers.clear()
                        currentSearchableUsers.addAll(users.filter { searchableUser ->
                            currentChosenUsers.none { chosenUser -> chosenUser.id == searchableUser.id }
                        })

                        if (currentSearchableUsers.isEmpty()) {
                            view?.showSearchableUsersEmpty("No users found matching '$query'.")
                        } else {
                            view?.hideSearchableUsersEmpty()
                        }
                        view?.displaySearchableUsers(ArrayList(currentSearchableUsers))
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "Error searching users", exception)
                        view?.displayError("Failed to search users: ${exception.localizedMessage}")
                        view?.showSearchableUsersEmpty("Error searching. Please try again.")
                    }
                )
            }
        }
    }

    override fun selectUser(user: User) {
        if (currentChosenUsers.any { it.id == user.id }) return

        currentChosenUsers.add(user)

        view?.addUserToChosenList(user)
        view?.displayChosenUsers(ArrayList(currentChosenUsers))


        if (currentChosenUsers.isEmpty()) {
            view?.showChosenUsersEmpty("No users selected.")
        } else {
            view?.hideChosenUsersEmpty()
        }
        updateInviteButtonState()
    }

    override fun deselectUser(user: User) {
        val removedFromChosen = currentChosenUsers.removeAll { it.id == user.id }
        if (removedFromChosen) {
            if (!currentSearchableUsers.any { it.id == user.id }) {
                currentSearchableUsers.add(0, user)
            }


            view?.removeUserFromChosenList(user)

            view?.displayChosenUsers(ArrayList(currentChosenUsers))
            view?.displaySearchableUsers(ArrayList(currentSearchableUsers))

            if (currentChosenUsers.isEmpty()) {
                view?.showChosenUsersEmpty("No users selected.")
            } else {
                view?.hideChosenUsersEmpty()
            }
            updateInviteButtonState()
        }
    }


    override fun onInviteClicked() {
        if (currentChosenUsers.isNotEmpty()) {
            val userDisplayNames =
                currentChosenUsers.joinToString { it.username ?: it.email ?: it.id }
            Log.i(TAG, "Invite button clicked. Chosen users: $userDisplayNames")
            view?.showError("Invite functionality for: $userDisplayNames (Not implemented yet)")
        }
    }

    private fun updateInviteButtonState() {
        val count = currentChosenUsers.size
        view?.updateInviteButton(count, count > 0)
    }
}
