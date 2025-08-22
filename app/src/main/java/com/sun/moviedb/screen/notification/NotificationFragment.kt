package com.sun.moviedb.screen.notification

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.sun.moviedb.data.model.Member
import com.sun.moviedb.data.repository.source.remote.NetworkResult
import com.sun.moviedb.utils.base.BaseFragment
import com.sun.moviedb.databinding.FragmentNotificationBinding
import com.sun.moviedb.screen.watchMovie.WatchMovieActivity
import com.sun.moviedb.utils.AppLocator
import com.sun.moviedb.utils.session.UserSession

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

        setupUI()
    }

    private fun setupUI() {
        binding.btnJoinRoom1.setOnClickListener {
            val input = binding.editTextText.text.toString()
            Log.d("NotificationFragment", "Room ID input: $input")

            if (input.isNotEmpty()) {
                goRoom(input)
                binding.editTextText.text.clear()
            } else {
                binding.editTextText.error = "Please enter a room ID"
            }

            navigateToWatch(input)
        }
    }

    private fun navigateToWatch(roomId: String) {
        val intent = Intent(requireContext(), WatchMovieActivity::class.java).apply {
            putExtra(ARG_M3U8_LINK, "")
            putExtra(ARG_ROOM_ID, roomId)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        }
        startActivity(intent)
    }

    private fun goRoom(roomId: String){
        val member = Member(
            memberId = UserSession.userId ?: "knot",
            memberName = UserSession.userName ?: "Unknown",
            linkAvatar = UserSession.linkAvatar ?: "",
            joinAt = System.currentTimeMillis(),
            isHost = false // Assuming the user is the host when creating the room
        )

        AppLocator.memberRepository.addMember(roomId, member) { result ->
            when (result) {
                is NetworkResult.OnSuccess -> {
                    Toast.makeText(
                        requireContext(),
                        "Member added successfully ${member.memberName}; Room: $roomId",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.OnError -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object{
        private const val KEY_SLUG = "slug"
        private const val ARG_M3U8_LINK = "m3u8_link"
        const val ARG_ROOM_ID = "room_id"
    }
}
