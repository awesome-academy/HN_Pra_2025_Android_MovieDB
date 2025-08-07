package com.sun.moviedb.screen.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.moviedb.R
import com.sun.moviedb.databinding.FragmentSettingsBinding
import com.sun.moviedb.utils.base.BaseFragment

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

}

