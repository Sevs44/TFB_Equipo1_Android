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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.R
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageRequest
import com.saulhervas.easychat.databinding.FragmentChatLogBinding
import com.saulhervas.easychat.domain.model.UserSession
import com.saulhervas.easychat.ui.chat.list.MessagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LIMIT_MESSAGES = 20
private const val OFFSET_0_MESSAGE_SENT = 0

@AndroidEntryPoint
class ChatLogFragment @Inject constructor() : Fragment() {
    private lateinit var binding: FragmentChatLogBinding
    private val viewModel: ChatLogViewModel by viewModels<ChatLogViewModel>()

    private val args: ChatLogFragmentArgs by navArgs()
    @Inject
    lateinit var userSession: UserSession
    private lateinit var nickUser: String
    private lateinit var idChat: String
    private var isOnlineUser: Boolean = true
    private var offset: Int = 0
    private var nMessages: Int = 0

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
        return binding.root
    }

    private fun configClickListeners() {
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
            }
            ivProfile.setOnClickListener() {
                val action = ChatLogFragmentDirections.actionChatLogToChatDetailFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun setIsOnlineUser() {
        if (isOnlineUser) {
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
        configClickListeners()
        setOnScrollRecyclerView()
        adjustNestedScrollViewFillPortKeyboardEvent()
        startTimerRefresh()
    }

    private fun startTimerRefresh() {
        viewModel.startPeriodicRefresh(idChat, OFFSET_0_MESSAGE_SENT, LIMIT_MESSAGES)
    }

    private fun setOnScrollRecyclerView() {
        binding.rvMessage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
                layoutManager?.let {
                    if (it.findLastVisibleItemPosition() == messagesAdapter.itemCount - 1) {
                        onScrolledToTop()
                    }
                }
            }
        })
    }

    private fun onScrolledToTop() {
        updateOffset()
        viewModel.getOpenChats(idChat, offset, LIMIT_MESSAGES)
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
                messagesAdapter.submitList(it)
                Log.i("TAG", "observeViewModel: list ==> $it")
            }
        }
        lifecycleScope.launch {
            viewModel.messagesCountState.collect {
                nMessages = it
            }
        }
        lifecycleScope.launch {
            viewModel.messagesSentState.collect {
                if (it) {
                    cleanText(binding.etSendMessage)
                    viewModel.getOpenChats(idChat, OFFSET_0_MESSAGE_SENT, LIMIT_MESSAGES)
                }
            }
        }
    }

    private fun updateOffset() {
        offset += LIMIT_MESSAGES
        if (offset > nMessages) {
            offset = nMessages
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
            Log.i("TAG", "setUpViewModel: id => $idChat")
            viewModel.getOpenChats(idChat, offset, LIMIT_MESSAGES)
        }
    }

    private fun getUserArgs() {
        idChat = args.idChat
        nickUser = args.nickTarget
        isOnlineUser = args.isUserOnline
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}


