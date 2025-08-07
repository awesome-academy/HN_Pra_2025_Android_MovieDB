package com.sun.moviedb.screen.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.moviedb.R
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

