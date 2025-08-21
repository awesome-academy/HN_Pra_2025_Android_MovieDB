package com.sun.moviedb.screen.room

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.moviedb.R
import com.sun.moviedb.databinding.FragmentRoomBinding
import com.sun.moviedb.utils.base.BaseFragment

class RoomFragment : BaseFragment<FragmentRoomBinding>(), RoomContract.View {

    private lateinit var presenter: RoomPrensenter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRoomBinding {
        return FragmentRoomBinding.inflate(inflater, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun initView() {
        super.initView()
    }

    override fun initData() {
        super.initData()
        presenter = RoomPrensenter()
        presenter.attachView(this)
    }

    override fun showLoading(isLoading: Boolean) {
        TODO("Not yet implemented")
    }

    override fun showError(message: String) {
        TODO("Not yet implemented")
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RoomFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
            }
    }
}

