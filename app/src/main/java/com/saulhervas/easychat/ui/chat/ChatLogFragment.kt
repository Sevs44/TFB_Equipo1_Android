package com.saulhervas.easychat.ui.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.saulhervas.easychat.R
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageRequest
import com.saulhervas.easychat.databinding.FragmentChatLogBinding
import com.saulhervas.easychat.domain.model.UserSession
import com.saulhervas.easychat.ui.chat.list.MessagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LIMIT_MESSAGES = 20

@AndroidEntryPoint
class ChatLogFragment @Inject constructor() : Fragment() {
    private lateinit var binding: FragmentChatLogBinding
    private val viewModel: ChatLogViewModel by activityViewModels<ChatLogViewModel>()

    private val args: ChatLogFragmentArgs by navArgs()
    @Inject lateinit var userSession: UserSession
    private lateinit var nickUser: String
    private lateinit var idChat: String
    private var isOnlineUser: Boolean = true
    private var offset: Int = 0

    private val messagesAdapter: MessagesAdapter by lazy {
        MessagesAdapter(userSession.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatLogBinding.inflate(inflater, container, false)
        inflateBinding()
        return binding.root
    }

    private fun inflateBinding() {
        binding.apply {
            tvNameUser.text = nickUser
            setIsOnlineUser()
            imBtnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnSend.setOnClickListener {
                val newMessage = NewMessageRequest(
                    idChat,
                    userSession.id,
                    etSendMessage.text.toString()
                )
                viewModel.sendMessage(newMessage)
                cleanText(etSendMessage)
                viewModel.getOpenChats(idChat, offset, LIMIT_MESSAGES)
            }
        }
    }

    private fun setIsOnlineUser() {
        if (isOnlineUser == true) {
            binding.tvOnline.text = getString(R.string.isUserOnline)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun cleanText(editText: EditText) {
        editText.setText("")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        observeViewModel()
        setupRecyclerView()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.messagesState.collect {
                Log.i("TAG", "observeViewModel: it $it")
                messagesAdapter.submitList(it.messageList)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvMessage.layoutManager =
            LinearLayoutManager(requireContext()).apply {
                reverseLayout = true
            }
        binding.rvMessage.adapter = messagesAdapter
    }

    private fun setUpViewModel() {
        lifecycleScope.launch {
            viewModel.getOpenChats(idChat, offset, LIMIT_MESSAGES)
        }
    }

    private fun getUserArgs() {
        idChat = args.idChat
        nickUser = args.nickTarget
        isOnlineUser = args.isUserOnline
    }
}