package com.saulhervas.easychat.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.saulhervas.easychat.databinding.FragmentChatLogBinding
import com.saulhervas.easychat.domain.model.messages_list.MessageItemModel
import com.saulhervas.easychat.ui.chat.list.MessagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatLogFragment : Fragment() {
    private lateinit var binding: FragmentChatLogBinding
    private val viewModel: ChatLogViewModel by activityViewModels<ChatLogViewModel>()
    private val args: ChatLogFragmentArgs by navArgs()
    private lateinit var token: String
    private lateinit var idUser: String
    private lateinit var idChat: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatLogBinding.inflate(inflater, container, false)
        binding.apply {
            imBtnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            //ivProfile.setOnClickListener {
            //}
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.messagesState.collect {
                Log.i("TAG", "observeViewModel: it $it")
                setupRecyclerView(it.messageList)
            }
        }
    }

    private fun setupRecyclerView(messages: ArrayList<MessageItemModel>?) {
        Log.i("TAG", "setupRecyclerView: messages => $messages")
        binding.rvMessage.layoutManager = LinearLayoutManager(requireContext()).apply {
            reverseLayout = true
        }
        binding.rvMessage.adapter =
            MessagesAdapter(messages, idUser)
    }

    private fun setUpViewModel() {
        lifecycleScope.launch {
            Log.i("TAG", "setUpViewModel: id => $idChat")
            viewModel.getOpenChats(token, idChat, offset = 0, limit = 5)
        }
    }

    private fun getUserArgs() {
        token = args.token
        idUser = args.idUser
        idChat = args.idChat
    }
}


