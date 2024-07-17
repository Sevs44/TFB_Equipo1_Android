package com.saulhervas.easychat.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.saulhervas.easychat.databinding.FragmentChatLogBinding
import com.saulhervas.easychat.domain.model.UserSingleton
import com.saulhervas.easychat.domain.model.messages_list.MessageItemModel
import com.saulhervas.easychat.ui.chat.list.MessagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

@AndroidEntryPoint
class ChatLogFragment : Fragment() {
    private lateinit var binding: FragmentChatLogBinding
    private val viewModel: ChatLogViewModel by viewModels()
    private val args: ChatLogFragmentArgs by navArgs()
    private lateinit var token: String
    private lateinit var id: String

    @Inject
    lateinit var user: UserSingleton

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
            ivProfile.setOnClickListener {

            }
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
        binding.rvMessage.layoutManager = LinearLayoutManager(requireContext()).apply {
            reverseLayout = true
        }
        binding.rvMessage.adapter =
            MessagesAdapter(messages, user.id)
        Log.i("TAG", "setupRecyclerView: $messages")
    }

    private fun setUpViewModel() {
        lifecycleScope.launch {
            viewModel.getOpenChats(token)
        }
    }

    private fun getUserArgs() {
        token = args.token
        id = args.id
        user.id  = "688"
        Log.i("TAG", "getUserArgs: $user")
    }
}


