package com.sun.moviedb.screen.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sun.moviedb.utils.base.BaseFragment
import com.sun.moviedb.databinding.FragmentMovieDetailBinding

class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieDetailBinding {
        return FragmentMovieDetailBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        super.initView()
        val slug = arguments?.getString(KEY_SLUG) ?: ""
        binding.tvDetail.text = slug
    }

    override fun initData() {
        super.initData()
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

