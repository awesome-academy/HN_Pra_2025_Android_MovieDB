package com.sun.moviedb.screen.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.helper.widget.Grid
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sun.moviedb.R
import com.sun.moviedb.data.model.Category
import com.sun.moviedb.data.model.Episode
import com.sun.moviedb.data.model.Movie
import com.sun.moviedb.data.model.ServerData
import com.sun.moviedb.data.repository.source.MovieRepository
import com.sun.moviedb.data.repository.source.remote.MovieRemoteDataSource
import com.sun.moviedb.utils.base.BaseFragment
import com.sun.moviedb.databinding.FragmentMovieDetailBinding
import com.sun.moviedb.screen.detail.adapter.EpsListAdapter
import com.sun.moviedb.screen.detail.adapter.ServerDataListApdater

class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>(), MovieDetailContract.View {
    private lateinit var epsListAdapter: EpsListAdapter
    private lateinit var serverDataListApdater: ServerDataListApdater
    private lateinit var presenter: MovieDetailPresenter
    private lateinit var movieInfo: Movie
    private lateinit var episodes: List<Episode>
    private var isFavourite = false

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieDetailBinding {
        return FragmentMovieDetailBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        super.initView()
        val slug = arguments?.getString(KEY_SLUG) ?: ""
    }

    override fun initData() {
        super.initData()
        presenter = MovieDetailPresenter(
            MovieRepository.getInstance(
                local = null,
                remote = MovieRemoteDataSource.getInstance(),
            )
        )
        presenter.attachView(this)

        showLoading(true)
        presenter.getDetail(arguments?.getString(KEY_SLUG) ?: "")

        onStartFromBeginningButtonClicked()
        onFavoriteButtonClicked()
        onWatchNowButtonClicked()
        onChillWithFriendButtonClicked()
    }

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) ViewGroup.VISIBLE else ViewGroup.GONE
        binding.frOverlay.visibility = if (isLoading) ViewGroup.VISIBLE else ViewGroup.GONE
    }

    override fun showError(message: String) {
        showLoading(false)
    }

    override fun onGetDetailSuccess(
        movie: Movie,
        episodes: List<Episode>
    ) {
        this.movieInfo = movie
        this.episodes = episodes
        Log.d("MovieDetailFragment", "MOvie: $movie")
        Log.d("MovieDetailFragment", "Episodes: $episodes")

        showLoading(false)

        setUI()
        setEpsListView()
    }

    override fun onGetDetailError(message: String) {
        Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
    }

    private fun setUI() {
        Glide.with(requireContext())
            .load(movieInfo.thumbUrl)
            .into(binding.imgThumb)
        binding.tvTitle.text = movieInfo.name
        binding.tvOriginName.text = movieInfo.originName
        binding.tvDescrption.text = movieInfo.content
        binding.tvTime.text = movieInfo.time
        binding.tvCate.text = getListCate(movieInfo.category)
        binding.tvQuality.text = movieInfo.quality
        binding.tvLang.text = movieInfo.lang
        binding.tvStatus.text = movieInfo.episodeCurrent
        binding.tvYear.text = movieInfo.year.toString()
    }

    private fun setEpsListView() {
        epsListAdapter = EpsListAdapter(episodes) { serverDatas ->
            setServerDataListView(serverDatas)
        }

        binding.rvListEps.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, false
        )
        binding.rvListEps.adapter = epsListAdapter

        if (episodes.isNotEmpty()) {
            /* *
            * Auto select the first serverdata
            * */
            epsListAdapter.selectFirstItem()
        }
    }

    private fun setServerDataListView(serverData: List<ServerData>) {
        binding.progressBar2.visibility = ViewGroup.VISIBLE

        //delay to simulate loading
        binding.rvListServerData.postDelayed({
            serverDataListApdater = ServerDataListApdater(serverData) { item ->
                // Handle click on server data
                Toast.makeText(requireContext(), "Link m3u8: $item", Toast.LENGTH_SHORT).show()
            }

            binding.rvListServerData.layoutManager = GridLayoutManager(
                requireContext(), 3,
                GridLayoutManager.VERTICAL, false
            )
            binding.rvListServerData.adapter = serverDataListApdater

            binding.progressBar2.visibility = ViewGroup.GONE
        }, 500)
    }

    private fun onStartFromBeginningButtonClicked() {
        binding.btnStartZero.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Start watching from zero",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onFavoriteButtonClicked() {
        binding.btnFav.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Add ${movieInfo.name} to favorite",
                Toast.LENGTH_SHORT
            ).show()
            isFavourite = !isFavourite
            if (isFavourite) {
                binding.btnFav.setImageResource(R.drawable.ic_favorite_red_24)
                presenter.onFavClicked(movieInfo, isFavourite)
            } else {
                binding.btnFav.setImageResource(R.drawable.ic_favorite_24)
                presenter.onFavClicked(movieInfo, isFavourite)
            }

        }
    }

    private fun onWatchNowButtonClicked(){
        Toast.makeText(requireContext(), "watch from current episode + position", Toast.LENGTH_SHORT).show()
    }

    private fun onChillWithFriendButtonClicked(){
        Toast.makeText(requireContext(), "Chill with friend", Toast.LENGTH_SHORT).show()
    }

    private fun onBackButtonClicked(){

    }


    /**
     * support to show category chip in UI
     * get 2 first categories
     * if only one category, return that category name
     * */
    private fun getListCate(cates: List<Category> = emptyList()): String {
        if (cates.size == 1) return cates[0].name
        if (cates.size >= 2) {
            val res = StringBuilder()
            res.append(cates[0].name).append(" - ").append(cates[1].name)
            return res.toString()
        }
        return "Unknown"
    }

    companion object {
        const val KEY_SLUG = "slug"
        fun newInstance(slug: String): MovieDetailFragment {
            val fragment = MovieDetailFragment()
            val args = Bundle().apply {
                putString(KEY_SLUG, slug)
            }
            fragment.arguments = args
            return fragment
        }
    }
}

