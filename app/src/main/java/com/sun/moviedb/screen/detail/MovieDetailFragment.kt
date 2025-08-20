package com.sun.moviedb.screen.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
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
import com.sun.moviedb.screen.detail.adapter.ServerDataListAdapter
import com.sun.moviedb.screen.watchMovie.WatchMovieActivity
import com.sun.moviedb.utils.navigation.AppNavigator
import com.sun.moviedb.utils.navigation.NavDestination

class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>(), MovieDetailContract.View {
    private lateinit var epsListAdapter: EpsListAdapter
    private lateinit var serverDataListApdapter: ServerDataListAdapter
    private lateinit var presenter: MovieDetailPresenter
    private lateinit var movieInfo: Movie
    private lateinit var episodes: List<Episode>
    private var isFavourite = false
    private var slug: String = ""

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieDetailBinding {
        return FragmentMovieDetailBinding.inflate(inflater, container, false)
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
        slug = arguments?.getString(KEY_SLUG) ?: ""
        presenter.getDetail(slug)
    }

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) ViewGroup.VISIBLE else ViewGroup.GONE
        binding.frOverlay.visibility = if (isLoading) ViewGroup.VISIBLE else ViewGroup.GONE
    }

    override fun showError(message: String) {
        showLoading(false)
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
        onStartFromBeginningButtonClicked()
        onFavoriteButtonClicked()
        onWatchNowButtonClicked()
        onChillWithFriendButtonClicked()
        onBackButtonClicked()
        onInviteFriendButtonClicked()
    }

    private fun setUI() {
        Glide.with(requireContext())
            .load(movieInfo.thumbUrl)
            .into(binding.imgThumb)
        binding.tvTitle.text = movieInfo.name
        binding.tvOriginName.text = movieInfo.originName
        binding.tvDescription.text = movieInfo.content
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
            serverDataListApdapter = ServerDataListAdapter(serverData) { item ->
                // Handle click on server data
                Toast.makeText(requireContext(), "Link m3u8: $item", Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), WatchMovieActivity::class.java).apply {
                    putExtra(ARG_M3U8_LINK, item)
                }
                startActivity(intent)

            }

//            binding.rvListServerData.layoutManager = GridLayoutManager(
//                requireContext(), 3,
//                GridLayoutManager.VERTICAL, false
//            )
            binding.rvListServerData.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, false
            )
            binding.rvListServerData.adapter = serverDataListApdapter

            binding.progressBar2.visibility = ViewGroup.GONE
        }, 1000)
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

    private fun onWatchNowButtonClicked() {
        val intent = Intent(requireContext(), WatchMovieActivity::class.java)


        binding.btnWatchNow.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Watch from current episode + position",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onChillWithFriendButtonClicked() {
        binding.btnWatchWithFriends.setOnClickListener {
            Toast.makeText(requireContext(), "Chill with friend", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onBackButtonClicked() {
        binding.btnBack.setOnClickListener {
//          TODO: ERROR - cause app crash when back
        //            requireActivity().supportFragmentManager.popBackStackImmediate()
            Toast.makeText(requireContext(), "Back to previous screen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onInviteFriendButtonClicked(){
        binding.btnInvite.setOnClickListener {
            AppNavigator.navigateTo(NavDestination.InviteFriendScreen, true)

        }
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
        const val ARG_M3U8_LINK = "m3u8_link"
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

