package com.sun.moviedb.screen.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.sun.moviedb.data.model.Category
import com.sun.moviedb.data.model.Episode
import com.sun.moviedb.data.model.Movie
import com.sun.moviedb.data.repository.source.MovieRepository
import com.sun.moviedb.data.repository.source.remote.MovieRemoteDataSource
import com.sun.moviedb.utils.base.BaseFragment
import com.sun.moviedb.databinding.FragmentMovieDetailBinding

class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>(), MovieDetailContract.View {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieDetailBinding {
        return FragmentMovieDetailBinding.inflate(inflater, container, false)
    }

    private lateinit var presenter : MovieDetailPresenter
    private lateinit var movieInfo : Movie
    private lateinit var episodes: List<Episode>

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
        showLoading(false)
        this.movieInfo = movie
        this.episodes = episodes
        Log.d("MovieDetailFragment", "MOvie: $movie")
        Log.d("MovieDetailFragment", "Episodes: $episodes")

        setUI()
    }

    private fun setUI(){
        Glide.with(requireContext())
            .load(movieInfo.thumbUrl)
            .into(binding.imgThumb)
        binding.tvTitle.text = movieInfo.name
        binding.tvSession.text = movieInfo.type
        binding.tvDescrption.text = movieInfo.content
        binding.tvTime.text = movieInfo.time
        binding.tvCate.text = getListCate(movieInfo.category)
        binding.tvQuality.text = movieInfo.quality



    }
    private fun getListCate(cates: List<Category> = emptyList()): String{
        if (cates.size == 1) return cates[0].name
        if (cates.size >= 2){
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

