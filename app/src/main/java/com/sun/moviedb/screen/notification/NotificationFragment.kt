package com.sun.moviedb.screen.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sun.moviedb.utils.base.BaseFragment
import com.sun.moviedb.databinding.FragmentNotificationBinding

class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNotificationBinding {
        return FragmentNotificationBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        super.initView()
    }

    override fun initData() {
        super.initData()
    }
}
