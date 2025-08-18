package com.sun.moviedb.screen.searchUser

import com.sun.moviedb.data.model.User


interface SearchUserContract {

    interface View {
        fun showLoading()
        fun hideLoading()

        fun displaySearchableUsers(users: List<User>)
        fun displayChosenUsers(users: List<User>)

        fun removeUserFromSearchableList(user: User)
        fun addUserToSearchableList(user: User)

        fun removeUserFromChosenList(user: User)
        fun addUserToChosenList(user: User)

        fun showSearchableUsersEmpty(message: String)
        fun hideSearchableUsersEmpty()
        fun showChosenUsersEmpty(message: String)
        fun hideChosenUsersEmpty()

        fun displayError(message: String)
        fun updateInviteButton(count: Int, isEnabled: Boolean)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun searchUsers(query: String)
        fun selectUser(user: User)
        fun deselectUser(user: User)
        fun getInitialChosenUsers()
        fun onInviteClicked()
    }
}
