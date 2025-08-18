package com.sun.moviedb.screen.searchUser

import com.sun.moviedb.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchUserPresenterImpl : SearchUserContract.Presenter {

    private var view: SearchUserContract.View? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val allFetchedUsersForCurrentQuery = mutableListOf<User>()
    private val searchableUsersList = mutableListOf<User>()
    private val chosenUsersList = mutableListOf<User>()
    private val persistedChosenUserIds = mutableSetOf<String>()

    override fun attachView(view: SearchUserContract.View) {
        this.view = view
        view.displayChosenUsers(ArrayList(chosenUsersList))
        updateInviteButtonStatus()
        updateEmptyStates()
    }

    override fun detachView() {
        this.view = null
        presenterScope.cancel()
    }

    override fun getInitialChosenUsers() {
        view?.displayChosenUsers(ArrayList(chosenUsersList))
        updateInviteButtonStatus()
        updateEmptyStates()
    }

    override fun searchUsers(query: String) {
        if (query.isBlank()) {
            allFetchedUsersForCurrentQuery.clear()
            searchableUsersList.clear()
            view?.displaySearchableUsers(emptyList())
            updateEmptyStates()
            return
        }

        view?.showLoading()
        presenterScope.launch {
            delay(500)

            val dummyResultsFromApi = mutableListOf<User>()
            if (query.equals("test", ignoreCase = true)) {
                dummyResultsFromApi.add(User("1", "Test User One", null))
                dummyResultsFromApi.add(User("2", "Another Test", null))
                dummyResultsFromApi.add(User("3", "Test User Three", null))
                dummyResultsFromApi.add(User("common1", "Common User A", null))
            } else if (query.contains("alice", ignoreCase = true)) {
                dummyResultsFromApi.add(User("4", "Alice Wonderland", "url_to_alice_image"))
            } else if (query.contains("bob", ignoreCase = true)) {
                dummyResultsFromApi.add(User("5", "Bob The Builder", null))
                dummyResultsFromApi.add(User("common1", "Common User A", null))
            }
            allFetchedUsersForCurrentQuery.clear()
            allFetchedUsersForCurrentQuery.addAll(dummyResultsFromApi)

            searchableUsersList.addAll(allFetchedUsersForCurrentQuery.filterNot { apiUser ->
                chosenUsersList.any { chosenUser -> chosenUser.id == apiUser.id }
            })

            view?.hideLoading()
            view?.displaySearchableUsers(ArrayList(searchableUsersList))
            updateEmptyStates()
        }
    }

    override fun selectUser(user: User) {
        if (chosenUsersList.any { it.id == user.id }) return

        searchableUsersList.remove(user)
        view?.displaySearchableUsers(ArrayList(searchableUsersList))

        chosenUsersList.add(user)
        persistedChosenUserIds.add(user.id)
        view?.displayChosenUsers(ArrayList(chosenUsersList))

        updateInviteButtonStatus()
        updateEmptyStates()
    }

    override fun deselectUser(user: User) {
        if (!chosenUsersList.any { it.id == user.id }) return

        chosenUsersList.remove(user)
        persistedChosenUserIds.remove(user.id)
        view?.displayChosenUsers(ArrayList(chosenUsersList))
        if (allFetchedUsersForCurrentQuery.any { it.id == user.id } &&
            !searchableUsersList.any { it.id == user.id }) {
            searchableUsersList.add(user)
        }
        view?.displaySearchableUsers(ArrayList(searchableUsersList))

        updateInviteButtonStatus()
        updateEmptyStates()
    }

    private fun updateInviteButtonStatus() {
        view?.updateInviteButton(chosenUsersList.size, chosenUsersList.isNotEmpty())
    }

    private fun updateEmptyStates() {
        if (searchableUsersList.isEmpty()) {
            val query = ""
            if (query.isBlank() && allFetchedUsersForCurrentQuery.isEmpty()) {
                view?.showSearchableUsersEmpty("Start searching to find users.")
            } else {
                view?.showSearchableUsersEmpty("No users found.")
            }
        } else {
            view?.hideSearchableUsersEmpty()
        }

        if (chosenUsersList.isEmpty()) {
            view?.showChosenUsersEmpty("No users chosen yet.")
        } else {
            view?.hideChosenUsersEmpty()
        }
    }

    override fun onInviteClicked() {
        if (chosenUsersList.isNotEmpty()) {
            println("Inviting users: ${chosenUsersList.joinToString { it.username }}")
        } else {
        }
    }
}
