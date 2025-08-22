package com.sun.moviedb.screen.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.sun.moviedb.R
import com.sun.moviedb.data.model.MessageModel
import com.sun.moviedb.data.repository.rtdb.ChatRepository
import com.sun.moviedb.data.repository.rtdb.ChatRepositoryImpl
import com.sun.moviedb.databinding.FragmentChatBinding
import com.sun.moviedb.screen.chat.adapter.ChatAdapter
import com.sun.moviedb.utils.base.BaseFragment
import kotlin.text.clear
import kotlin.toString

class ChatFragment : BaseFragment<FragmentChatBinding>(), ChatContract.View {

    private lateinit var chatRepository: ChatRepository
    private lateinit var presenter: ChatContract.Presenter
    private lateinit var adapter: ChatAdapter
    private var messageList = mutableListOf<MessageModel>()
    private var roomId: String = "B64pKEV0PxWFTXOiCXSFCek82z53_PgfNZMdZATUusKY8EAHDykGwlAP2"
    private val TAG = "ChatFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatBinding {
        return FragmentChatBinding.inflate(inflater, container, false)
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
        // Initialize presenter
        chatRepository = ChatRepositoryImpl.getInstance()
        presenter = ChatPresenter(chatRepository)
        presenter.attachView(this)
        // Load messages
        presenter.receiveMessages(roomId)

        // Initialize adapter
        adapter = ChatAdapter(messageList)
        binding.rvLayoutChat.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvLayoutChat.adapter = adapter

        setupUI()
    }

    private fun setupUI() {
        binding.btnSendMessage.setOnClickListener {
            var message = binding.edtMessage.text.toString().trim()

            Log.d(TAG, "message: $message")
            if (message.isNotEmpty()) {

                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId == null)
                    showError("User not authenticated")
                else {
                    val messageModel = MessageModel(
                        senderId = userId,
                        senderName = "User 1",
                        content = message,
                        createAt = System.currentTimeMillis()
                    )
                    presenter.sendMessage(roomId, messageModel)
                    binding.edtMessage.text.clear() // Clear the input field after sending
                }
            } else {
                showError("Message cannot be empty")
            }
        }
    }

    override fun addMessages(message: MessageModel) {
        messageList.add(message)
        adapter.notifyItemInserted(messageList.size - 1)
        binding.rvLayoutChat.scrollToPosition(messageList.size - 1)
    }

    override fun showLoading(isLoading: Boolean) {
        TODO("Not yet implemented")
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), "$TAG: $message", Toast.LENGTH_SHORT).show()
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {

            }
    }
}

