package com.sun.moviedb.screen.notification

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.moviedb.data.model.Member
import com.sun.moviedb.data.model.NotificationModel
import com.sun.moviedb.data.repository.source.remote.NetworkResult
import com.sun.moviedb.databinding.FragmentNotificationBinding
import com.sun.moviedb.screen.notification.adapter.NotificationAdapter
import com.sun.moviedb.screen.watchMovie.WatchMovieActivity
import com.sun.moviedb.utils.AppLocator
import com.sun.moviedb.utils.base.BaseFragment
import com.sun.moviedb.utils.session.UserSession

class NotificationFragment : BaseFragment<FragmentNotificationBinding>(), NotificationContract.View {

    private lateinit var presenter: NotificationContract.Presenter
    private lateinit var adapter: NotificationAdapter
    val now = System.currentTimeMillis()
    private val TAG = "NotificationFragment"

    val fakeNotifications = mutableListOf(
        NotificationModel.Invite(
            id = "invite_1",
            title = "Mời bạn tham gia xem phim",
            body = "Bạn được mời vào phòng xem phim Inception lúc 20:00",
            createAt = now - 5 * 60 * 1000L, // 5 phút trước
            isRead = false,
            roomId = "room_1",
            roomName = "Phòng Inception",
            senderId = "user_1",
            senderName = "Minh Nguyễn",
            senderAvatar = "https://randomuser.me/api/portraits/men/32.jpg",
            movieId = "movie_inception",
            movieSlug = "inception"
        ),
        NotificationModel.Invite(
            id = "invite_2",
            title = "Lời mời kết bạn",
            body = "Người dùng A vừa gửi lời mời tham gia nhóm xem phim",
            createAt = now - 2 * 60 * 60 * 1000L, // 2 giờ trước
            isRead = true,
            roomId = "room_2",
            roomName = "Phòng Avatar",
            senderId = "user_2",
            senderName = "Linh Trần",
            senderAvatar = "https://randomuser.me/api/portraits/women/65.jpg",
            movieId = "movie_avatar",
            movieSlug = "avatar"
        ),
        NotificationModel.Invite(
            id = "invite_3",
            title = "Mời xem phim mới",
            body = "Bạn được mời xem phim Interstellar vào lúc 21:00",
            createAt = now - 24 * 60 * 60 * 1000L, // 1 ngày trước
            isRead = false,
            roomId = "room_3",
            roomName = "Phòng Interstellar",
            senderId = "user_3",
            senderName = "An Phạm",
            senderAvatar = "https://i.pravatar.cc/150?img=15",
            movieId = "movie_interstellar",
            movieSlug = "interstellar"
        ),
        NotificationModel.Invite(
            id = "invite_4",
            title = "Xem phim The Matrix",
            body = "Anh em tụ tập xem Matrix tối nay không?",
            createAt = now - 3 * 24 * 60 * 60 * 1000L, // 3 ngày trước
            isRead = true,
            roomId = "room_4",
            roomName = "Phòng Matrix",
            senderId = "user_4",
            senderName = "Hoàng Dương",
            senderAvatar = "https://i.pravatar.cc/150?img=25",
            movieId = "movie_matrix",
            movieSlug = "the-matrix"
        ),
        NotificationModel.Invite(
            id = "invite_5",
            title = "🎬 Phòng phim mới mở",
            body = "Bạn được mời vào phòng Doctor Strange lúc 22:00",
            createAt = now - 7 * 24 * 60 * 60 * 1000L, // 7 ngày trước
            isRead = false,
            roomId = "room_5",
            roomName = "Phòng Doctor Strange",
            senderId = "user_5",
            senderName = "Trúc Mai",
            senderAvatar = "https://picsum.photos/seed/avatar1/100",
            movieId = "movie_doctor_strange",
            movieSlug = "doctor-strange"
        ),
        NotificationModel.System(
            id = "system_1",
            title = "Thông báo hệ thống",
            body = "Hệ thống sẽ bảo trì lúc 23:00 tối nay",
            createAt = now - 10 * 60 * 1000L, // 10 phút trước
            isRead = false
        ),
        NotificationModel.System(
            id = "system_2",
            title = "⚠Cảnh báo bảo mật",
            body = "Có thiết bị lạ đăng nhập vào tài khoản của bạn",
            createAt = now - 3 * 60 * 60 * 1000L, // 3 giờ trước
            isRead = true
        ),
        NotificationModel.System(
            id = "system_3",
            title = "Cập nhật mới",
            body = "Tính năng xem cùng bạn bè đã được cải tiến",
            createAt = now - 2 * 24 * 60 * 60 * 1000L, // 2 ngày trước
            isRead = false
        ),
        NotificationModel.System(
            id = "system_4",
            title = "Đăng nhập bất thường",
            body = "Chúng tôi phát hiện hoạt động bất thường từ tài khoản của bạn",
            createAt = now - 10 * 24 * 60 * 60 * 1000L, // 10 ngày trước
            isRead = true
        ),
        NotificationModel.System(
            id = "system_5",
            title = "Chào mừng bạn!",
            body = "Cảm ơn bạn đã đăng ký tài khoản, hãy cùng xem phim!",
            createAt = now - 30 * 24 * 60 * 60 * 1000L, // 30 ngày trước
            isRead = false
        )
    )


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNotificationBinding {
        return FragmentNotificationBinding.inflate(inflater, container, false)
    }

    override fun initData() {
        super.initData()

        presenter = NotificationPresenter()
        presenter.attachView(this)
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    private fun setupUI() {
        setupStatusBarPadding()
        adapter = NotificationAdapter(fakeNotifications){roomId, movieSlug ->
            Log.d(TAG, "Notification clicked: Room ID = $roomId, Movie Slug = $movieSlug")
            Toast.makeText(requireContext(), "Clicked on room: $roomId & movieSlug: $movieSlug", Toast.LENGTH_SHORT).show()
        }
        binding.rvListNotifications.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)
        binding.rvListNotifications.adapter = adapter


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

    private fun setupStatusBarPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
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

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar4.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        private const val KEY_SLUG = "slug"
        private const val ARG_M3U8_LINK = "m3u8_link"
        const val ARG_ROOM_ID = "room_id"
    }
}
