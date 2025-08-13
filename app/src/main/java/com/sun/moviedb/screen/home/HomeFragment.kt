package com.sun.moviedb.screen.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sun.moviedb.utils.base.BaseFragment
import com.sun.moviedb.databinding.FragmentHomeBinding
import com.sun.moviedb.utils.navigation.AppNavigator
import com.sun.moviedb.utils.navigation.NavDestination


class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initView() {
         
    }

    override fun initData() {
        binding.btnDetail.setOnClickListener {
            AppNavigator.navigateTo(NavDestination.MovieDetailScreen("ngoi-truong-xac-song"), true)
        }
    }
}

