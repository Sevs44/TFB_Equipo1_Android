package com.saulhervas.easychat.ui.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageRequest
import com.saulhervas.easychat.databinding.FragmentChatLogBinding
import com.saulhervas.easychat.domain.model.UserSession
import com.saulhervas.easychat.domain.model.messages_list.MessageItemModel
import com.saulhervas.easychat.ui.chat.list.MessagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val LIMIT_MESSAGES = 20

@AndroidEntryPoint
class ChatLogFragment : Fragment() {
    private lateinit var binding: FragmentChatLogBinding
    private val viewModel: ChatLogViewModel by activityViewModels<ChatLogViewModel>()

    private val args: ChatLogFragmentArgs by navArgs()
    private val userSession = UserSession()
    private lateinit var nickUser: String
    private lateinit var idChat: String
    private var offset: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun configClickListeners() {
        binding.apply {
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
            //ivProfile.setOnClickListener {
            //}
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
        configClickListeners()
        adjustNestedScrollViewFillPortKeyboardEvent()
    }

    private fun adjustNestedScrollViewFillPortKeyboardEvent() {
        val rootView = binding.root
        val nsvContainer = binding.nsvContainer

        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            ViewCompat.getRootWindowInsets(rootView)?.let { insets ->
                val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                val navBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                val keypadHeight = imeHeight - navBarHeight

                if (keypadHeight > 0) {
                    nsvContainer.setPadding(0, 0, 0, keypadHeight)
                } else {
                    nsvContainer.setPadding(0, 0, 0, 0)
                }
            }
        }
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
        binding.rvMessage.layoutManager =
            LinearLayoutManager(requireContext()).apply {
            reverseLayout = true
        }
        binding.rvMessage.adapter = MessagesAdapter(messages, userSession.id)
    }

    private fun setUpViewModel() {
        lifecycleScope.launch {
            Log.i("TAG", "setUpViewModel: id => $idChat")
            viewModel.getOpenChats(idChat, offset, LIMIT_MESSAGES)
        }
    }

    private fun getUserArgs() {
        idChat = args.idChat
        nickUser = args.nickTarget
    }
}


