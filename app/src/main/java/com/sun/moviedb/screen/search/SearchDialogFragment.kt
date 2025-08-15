package com.sun.moviedb.screen.search

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
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
import com.sun.moviedb.data.model.Item
import com.sun.moviedb.data.repository.source.MovieRepository
import com.sun.moviedb.data.repository.source.remote.MovieRemoteDataSource
import com.sun.moviedb.databinding.FragmentSearchBottomSheetBinding

class SearchDialogFragment : BottomSheetDialogFragment(), SearchView {
    private var _binding: FragmentSearchBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val sheetHeightRatio = 0.95f
    private lateinit var presenter: SearchPresenter
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var languageChipAdapter: LanguageChipAdapter
    private var currentSortLang: String? = null
    private var languageList: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieRepository = MovieRepository.getInstance(MovieRemoteDataSource.getInstance())
        presenter = SearchPresenterImpl(movieRepository)
        presenter.attachView(this)
        setupLanguageRecyclerView()
        setupMovieRecyclerView()
        setupSearchInput()
        presenter.loadLanguages()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    private fun setupLanguageRecyclerView() {
        languageChipAdapter = LanguageChipAdapter { selectedLang ->
            currentSortLang = selectedLang
            val keyword = binding.searchEdit.text?.toString()?.trim() ?: ""
            if (keyword.isNotEmpty()) {
                presenter.searchMovie(keyword, currentSortLang, 1)
            }
        }
        binding.languageRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = languageChipAdapter
        }
    }

    private fun setupMovieRecyclerView() {
        searchAdapter = SearchAdapter { item ->
            Toast.makeText(requireContext(), "Clicked: ${item.name}", Toast.LENGTH_SHORT).show()
        }
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

    private fun setupSearchInput() {
        binding.searchBtn.setOnClickListener {
            val keyword = binding.searchEdit.text.toString().trim()
            if (keyword.isNotEmpty()) {
                binding.tvEmptyHint.visibility = View.GONE
                presenter.searchMovie(keyword, currentSortLang, 1)
            } else {
                binding.tvEmptyHint.visibility = View.VISIBLE
                presenter.clearSearch()
            }
        }
    }

    override fun showLoading(isLoading: Boolean) {
        binding.layoutLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun showSearchResults(items: List<Item>) {
        searchAdapter.submitList(items)
        binding.topSearchesTitle.visibility = View.VISIBLE
        binding.tvEmptyResult.visibility = View.GONE
    }

    override fun showEmptyResult() {
        searchAdapter.submitList(emptyList())
        binding.tvEmptyResult.visibility = View.VISIBLE
        binding.topSearchesTitle.visibility = View.GONE
        binding.tvEmptyHint.visibility = View.GONE
    }

    override fun showError(message: String) {
        binding.layoutLoading.visibility = View.GONE
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoadMoreError() {
        Toast.makeText(context, "Load more error", Toast.LENGTH_SHORT).show()
    }

    override fun showLanguages(languages: List<String>) {
        languageList = languages
        languageChipAdapter.submitList(languageList)
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
}
