package com.sun.moviedb.screen.search

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sun.moviedb.MyApp
import com.sun.moviedb.data.model.Item
import com.sun.moviedb.databinding.FragmentSearchBottomSheetBinding
import com.sun.moviedb.screen.search.adapter.LanguageChipAdapter
import com.sun.moviedb.screen.search.adapter.SearchAdapter
import com.sun.moviedb.screen.search.adapter.SearchHistoryAdapter

import com.sun.moviedb.utils.navigation.AppNavigator
import com.sun.moviedb.utils.navigation.NavDestination

class SearchDialogFragment : BottomSheetDialogFragment(), SearchContract.SearchView {
    private var _binding: FragmentSearchBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val sheetHeightRatio = 0.95f
    private lateinit var presenter: SearchContract.SearchPresenter
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var languageChipAdapter: LanguageChipAdapter
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private var currentSortLang: String? = null
    private var languageList: List<String> = emptyList()

    private enum class SearchUIState { HISTORY, RESULTS, EMPTY_HINT, EMPTY_RESULT }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app = requireActivity().application as MyApp
        presenter = SearchPresenter(app.movieRepository)

        presenter.attachView(this)

        setupLanguageRecyclerView()
        setupMovieRecyclerView()
        setupSearchHistoryRecyclerView()
        setupSearchInput()
        presenter.loadLanguages()
        setupKeyboardAndHistoryBehavior()
    }

    private fun hideKeyboardAndClearFocus() {
        val imm =
            requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEdit.windowToken, 0)
        binding.searchEdit.clearFocus()
    }

    private fun updateSearchUI(state: SearchUIState) = with(binding) {
        when (state) {
            SearchUIState.HISTORY -> {
                searchHistoryRecyclerView.visibility = View.VISIBLE
                historyTitle.visibility = View.VISIBLE
                searchMovieRecyclerView.visibility = View.GONE
                topSearchesTitle.visibility = View.GONE
                tvEmptyHint.visibility = View.GONE
                tvEmptyResult.visibility = View.GONE
            }

            SearchUIState.RESULTS -> {
                searchHistoryRecyclerView.visibility = View.GONE
                historyTitle.visibility = View.GONE
                searchMovieRecyclerView.visibility = View.VISIBLE
                topSearchesTitle.visibility = View.VISIBLE
                tvEmptyHint.visibility = View.GONE
                tvEmptyResult.visibility = View.GONE
            }

            SearchUIState.EMPTY_HINT -> {
                tvEmptyHint.visibility = View.VISIBLE
                searchMovieRecyclerView.visibility = View.GONE
                searchHistoryRecyclerView.visibility = View.GONE
                historyTitle.visibility = View.GONE
                topSearchesTitle.visibility = View.GONE
                tvEmptyResult.visibility = View.GONE
            }

            SearchUIState.EMPTY_RESULT -> {
                tvEmptyResult.visibility = View.VISIBLE
                searchMovieRecyclerView.visibility = View.GONE
                topSearchesTitle.visibility = View.GONE
                tvEmptyHint.visibility = View.GONE
                searchHistoryRecyclerView.visibility = View.GONE
                historyTitle.visibility = View.GONE
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupKeyboardAndHistoryBehavior() {
        binding.root.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_DOWN) {
                val outRect = android.graphics.Rect()
                binding.searchEdit.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    hideKeyboardAndClearFocus()
                }
            }
            false
        }

        binding.searchEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                presenter.getSearchHistory()
            }
        }

        binding.searchEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.getSearchHistory()
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun setupSearchHistoryRecyclerView() {
        searchHistoryAdapter = SearchHistoryAdapter { keyword ->
            binding.searchEdit.setText(keyword)
            binding.searchEdit.setSelection(keyword.length)
            hideKeyboardAndClearFocus()
            updateSearchUI(SearchUIState.RESULTS)
            presenter.insertSearchHistory(keyword)
            presenter.searchMovie(keyword, currentSortLang, 1)
        }
        binding.searchHistoryRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.searchHistoryRecyclerView.adapter = searchHistoryAdapter
    }

    private fun setupSearchInput() {
        binding.searchBtn.setOnClickListener {
            val keyword = binding.searchEdit.text.toString().trim()
            if (keyword.isNotEmpty()) {
                hideKeyboardAndClearFocus()
                updateSearchUI(SearchUIState.RESULTS)
                presenter.insertSearchHistory(keyword)
                presenter.searchMovie(keyword, currentSortLang, 1)
            } else {
                updateSearchUI(SearchUIState.EMPTY_HINT)
                presenter.clearSearch()
            }
        }
    }

    private fun setupMovieRecyclerView() {
        searchAdapter = SearchAdapter { item -> onMovieClick(item) }
        binding.searchMovieRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.searchMovieRecyclerView.adapter = searchAdapter
        binding.searchMovieRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                if (lastVisible >= totalItemCount - 1) {
                    presenter.loadMoreResults()
                }
            }
        })
    }

    private fun setupLanguageRecyclerView() {
        languageChipAdapter = LanguageChipAdapter { selectedLang ->
            currentSortLang = selectedLang
            val keyword = binding.searchEdit.text?.toString()?.trim() ?: ""
            if (keyword.isNotEmpty()) {
                updateSearchUI(SearchUIState.RESULTS)
                presenter.searchMovie(keyword, currentSortLang, 1)
            }
        }
        binding.languageRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = languageChipAdapter
        }
    }

    override fun showLoading(isLoading: Boolean) {
        binding.layoutLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun showSearchResults(items: List<Item>) {
        searchAdapter.submitList(items)
        updateSearchUI(SearchUIState.RESULTS)
    }

    override fun showEmptyResult() {
        searchAdapter.submitList(emptyList())
        updateSearchUI(SearchUIState.EMPTY_RESULT)
    }

    override fun showError(message: String) {
        binding.layoutLoading.visibility = View.GONE
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLanguages(languages: List<String>) {
        languageList = languages
        languageChipAdapter.submitList(languageList)
    }

    override fun showSearchHistory(keywords: List<String>) {
        val query = binding.searchEdit.text?.toString() ?: ""
        val filtered = if (query.isEmpty()) keywords else keywords.filter {
            it.contains(query, ignoreCase = true)
        }
        if (filtered.isNotEmpty()) {
            searchHistoryAdapter.submitList(filtered)
            updateSearchUI(SearchUIState.HISTORY)
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as? BottomSheetDialog
        val bottomSheet =
            dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val layoutParams = it.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            it.layoutParams = layoutParams
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            setupBottomSheet(dialog)
        }
        return dialog
    }

    private fun setupBottomSheet(bottomSheetDialog: BottomSheetDialog) {
        val sheet = bottomSheetDialog.findViewById<FrameLayout>(
            com.google.android.material.R.id.design_bottom_sheet
        )
        sheet?.let {
            it.setBackgroundColor(Color.TRANSPARENT)
            val params = it.layoutParams
            params.height = getSheetHeight()
            it.layoutParams = params
            BottomSheetBehavior.from(it).apply {
                peekHeight = getSheetHeight()
                state = BottomSheetBehavior.STATE_EXPANDED
                isDraggable = false
            }
        }
    }

    private fun getSheetHeight(): Int {
        return (resources.displayMetrics.heightPixels * sheetHeightRatio).toInt()
    }

    private fun onMovieClick(item: Item) {
        dismiss()
        AppNavigator.navigateTo(NavDestination.MovieDetailScreen(item.slug), addToBackStack = true)
    }
}
