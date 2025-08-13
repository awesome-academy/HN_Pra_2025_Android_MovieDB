package com.sun.moviedb.screen.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sun.moviedb.databinding.FragmentFilterBinding
import com.sun.moviedb.utils.base.BaseFragment

class FilterFragment : BaseFragment<FragmentFilterBinding>(){

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterBinding {
        return FragmentFilterBinding.inflate(inflater, container, false)
    }

}

