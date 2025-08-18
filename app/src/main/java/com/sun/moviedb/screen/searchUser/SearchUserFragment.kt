package com.sun.moviedb.screen.searchUser

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.search.SearchBar
import com.sun.moviedb.screen.searchUser.adapter.ChosenUserRecyclerAdapter
import com.sun.moviedb.screen.searchUser.adapter.SearchUserRecyclerAdapter
import com.sun.moviedb.R
import com.sun.moviedb.data.model.User

class SearchUserFragment : Fragment(), SearchUserContract.View {

    private lateinit var presenter: SearchUserContract.Presenter // Presenter is essential in MVP

    private var searchBar: SearchBar? = null
    private var searchUserRecyclerView: RecyclerView? = null
    private var chosenUserRecyclerView: RecyclerView? = null
    private var inviteButton: Button? = null
    private var progressBar: ProgressBar? = null
    private var emptyStateTextView: TextView? = null
    private var emptyStateChosenTextView: TextView? = null

    private lateinit var searchUserAdapter: SearchUserRecyclerAdapter
    private lateinit var chosenUserAdapter: ChosenUserRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = SearchUserPresenterImpl()
        presenter.attachView(this)

        searchBar = view.findViewById(R.id.searchBarWatchParty)
        searchUserRecyclerView = view.findViewById(R.id.recyclerViewWatchParty)
        chosenUserRecyclerView = view.findViewById(R.id.recyclerViewWatchPartyChosen)
        inviteButton = view.findViewById(R.id.inviteButtonWatchParty)
        progressBar = view.findViewById(R.id.progressBarSearch)
        emptyStateTextView = view.findViewById(R.id.textViewEmptyState)
        emptyStateChosenTextView = view.findViewById(R.id.textViewEmptyStateChosen)

        setupRecyclerViews()
        setupStandaloneSearchBar()
        setupInviteButton()

        presenter.getInitialChosenUsers()
    }

    private fun setupRecyclerViews() {
        searchUserAdapter = SearchUserRecyclerAdapter { user ->
            presenter.selectUser(user)
        }
        searchUserRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchUserAdapter
        }

        chosenUserAdapter = ChosenUserRecyclerAdapter { user ->
            presenter.deselectUser(user)
        }
        chosenUserRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = chosenUserAdapter
        }
    }

    private fun setupStandaloneSearchBar() {
        searchBar?.hint = getString(R.string.search_for_people)
        searchBar?.setOnClickListener {
            showSearchInputDialog()
        }
        searchBar?.setNavigationOnClickListener {
            searchBar?.setText("")
            presenter.searchUsers("")
        }
    }

    private fun showSearchInputDialog() {
        val editText = EditText(requireContext()).apply {
            hint = getString(R.string.search_for_people)
            setText(searchBar?.text?.takeIf { it.isNotBlank() && it.toString() != getString(R.string.search_for_people) } ?: "")
            setSingleLine(true)
            inputType = InputType.TYPE_CLASS_TEXT
            imeOptions = EditorInfo.IME_ACTION_SEARCH
        }

//        val dialog = AlertDialog.Builder(requireContext())
//            .setTitle(getString(R.string.search_dialog_title))
//            .setView(editText)
//            .setPositiveButton(getString(R.string.search_action)) { dialogInterface, _ ->
//                val query = editText.text.toString().trim()
//                searchBar?.text = query // Update SearchBar display
//                presenter.searchUsers(query)
//                dialogInterface.dismiss()
//            }
//            .setNegativeButton(getString(R.string.cancel_action)) { dialogInterface, _ ->
//                dialogInterface.cancel()
//            }
//            .create()

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = editText.text.toString().trim()
                searchBar?.setText(query)
                presenter.searchUsers(query)
//                dialog.dismiss()
                true
            } else {
                false
            }
        }
//        dialog.show()
    }

    private fun setupInviteButton() {
        inviteButton?.setOnClickListener {
            presenter.onInviteClicked()
        }
    }

    override fun showLoading() {
        progressBar?.visibility = View.VISIBLE
        searchUserRecyclerView?.visibility = View.GONE
        emptyStateTextView?.visibility = View.GONE
    }

    override fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    override fun displaySearchableUsers(users: List<User>) {
        searchUserAdapter.submitList(users)
    }

    override fun displayChosenUsers(users: List<User>) {
        chosenUserAdapter.submitList(users)
    }

    override fun removeUserFromSearchableList(user: User) {

        Log.d("SearchUserFragment", "Presenter requested removeUserFromSearchableList (not directly implemented, rely on displaySearchableUsers)")
    }

    override fun addUserToSearchableList(user: User) {
        Log.d("SearchUserFragment", "Presenter requested addUserToSearchableList (not directly implemented, rely on displaySearchableUsers)")
    }

    override fun removeUserFromChosenList(user: User) {
        Log.d("SearchUserFragment", "Presenter requested removeUserFromChosenList (not directly implemented, rely on displayChosenUsers)")
    }

    override fun addUserToChosenList(user: User) {
        Log.d("SearchUserFragment", "Presenter requested addUserToChosenList (not directly implemented, rely on displayChosenUsers)")
    }

    override fun showSearchableUsersEmpty(message: String) {
        emptyStateTextView?.text = message
        emptyStateTextView?.visibility = View.VISIBLE
        searchUserRecyclerView?.visibility = View.GONE
    }

    override fun hideSearchableUsersEmpty() {
        emptyStateTextView?.visibility = View.GONE
        searchUserRecyclerView?.visibility = View.VISIBLE
    }

    override fun showChosenUsersEmpty(message: String) {
        emptyStateChosenTextView?.text = message
        emptyStateChosenTextView?.visibility = View.VISIBLE
    }

    override fun hideChosenUsersEmpty() {
        emptyStateChosenTextView?.visibility = View.GONE
    }

    override fun displayError(message: String) {
        hideLoading()
        Toast.makeText(context, "Search Error: $message", Toast.LENGTH_LONG).show()
        emptyStateTextView?.text = "Error: $message"
        emptyStateTextView?.visibility = View.VISIBLE
        searchUserRecyclerView?.visibility = View.GONE
    }

    override fun updateInviteButton(count: Int, isEnabled: Boolean) {
        inviteButton?.isEnabled = isEnabled
        inviteButton?.text = if (isEnabled) "Invite ($count)" else getString(R.string.invite)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
        searchBar = null
        searchUserRecyclerView = null
        chosenUserRecyclerView = null
        inviteButton = null
        progressBar = null
        emptyStateTextView = null
        emptyStateChosenTextView = null
    }
}
