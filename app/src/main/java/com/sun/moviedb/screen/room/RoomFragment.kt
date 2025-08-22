package com.sun.moviedb.screen.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.moviedb.R
import com.sun.moviedb.data.model.Member
import com.sun.moviedb.databinding.FragmentRoomBinding
import com.sun.moviedb.screen.room.adapter.RoomAdapter
import com.sun.moviedb.screen.watchMovie.WatchMovieContract
import com.sun.moviedb.utils.base.BaseFragment
import com.sun.moviedb.utils.session.RoomSession

class RoomFragment : BaseFragment<FragmentRoomBinding>() {

    private lateinit var roomAdapter: RoomAdapter
    private val memberList = mutableListOf<Member>()

    private var presenter: WatchMovieContract.Presenter? = null

    fun setPresenter(presenter: WatchMovieContract.Presenter) {
        this.presenter = presenter
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRoomBinding {
        return FragmentRoomBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        super.initView()
        val searchUserBackground = binding.root.findViewById<View>(R.id.searchUserBackground)
        searchUserBackground.setBackgroundResource(R.drawable.bg_transparent)
    }

    override fun initData() {
        super.initData()
        setupUI()
    }

    private fun setupUI() {
        if (presenter == null)
            throw Exception("Presenter is null, please set it before using RoomFragment")

        roomAdapter = RoomAdapter { choosenMember ->
            val currentRoomId = RoomSession.roomId
            if (currentRoomId == null)
                throw Exception("Room Id is null, please set it before removing member in HomeFragment")
            presenter!!.removeChoosenMember(currentRoomId, choosenMember)
            roomAdapter.removeItem(choosenMember)
        }

        /*
        * get current list of members if exists
        * */
        roomAdapter.setItems(presenter!!.getCachedMembers())

        binding.rvListMember.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvListMember.adapter = roomAdapter

    }

    fun updateMembers(members: List<Member>) {
        memberList.clear()
        memberList.addAll(members)
        roomAdapter.setItems(memberList)
    }

}

