package com.sun.moviedb.screen

import com.sun.moviedb.utils.base.BaseActivity
import com.sun.moviedb.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView() {

    }
}
