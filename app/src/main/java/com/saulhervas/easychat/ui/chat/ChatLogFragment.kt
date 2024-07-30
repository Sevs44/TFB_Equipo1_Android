package com.saulhervas.easychat.ui.chat

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LIMIT_MESSAGES = 100
private const val OFFSET_0_MESSAGE_SENT = 0

@AndroidEntryPoint
class ChatLogFragment @Inject constructor() : Fragment() {
    private lateinit var binding: FragmentChatLogBinding
    private val viewModel: ChatLogViewModel by viewModels()

    private val args: ChatLogFragmentArgs by navArgs()
    @Inject
    lateinit var userSession: UserSession
    private lateinit var nickUser: String
    private lateinit var idChat: String
    private var colorMap: String = ""
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
        inflateBinding()
        return binding.root
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

    private fun setUpViewModel() {
        lifecycleScope.launch {
            viewModel.getMessages(idChat, offset, LIMIT_MESSAGES)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch(Dispatchers.IO) {
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
            viewModel.messagesSentShared.collect {
                if (it) {
                    cleanText(binding.etSendMessage)
                    viewModel.getMessages(idChat, OFFSET_0_MESSAGE_SENT, LIMIT_MESSAGES)
                    binding.rvMessage.smoothScrollToPosition(0)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.loadingShared.collect {
                if (it) {
                    showProgressBar()
                } else {
                    hideProgressBar()
                }
            }
        }
    }

    private fun inflateBinding() {
        binding.tvNameUser.text = nickUser
        binding.tvInitial.text = nickUser.firstOrNull()?.uppercase().toString()

        val colorInt = if (colorMap.isNotEmpty()) {
            try {
                colorMap.toInt()
            } catch (e: NumberFormatException) {
                Log.e("ChatLogFragment", "Error parsing color: $colorMap", e)
                Color.TRANSPARENT
            }
        } else {
            Color.TRANSPARENT
        }

        binding.tvInitial.background = createCircleDrawable(colorInt)
        setIsOnlineUser()
    }

    private fun createCircleDrawable(color: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color)
        }
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
            tvInitial.setOnClickListener {
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
        viewModel.getMessages(idChat, offset, LIMIT_MESSAGES)
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

    private fun getUserArgs() {
        idChat = args.idChat
        nickUser = args.nickTarget
        isOnlineUser = args.isUserOnline
        colorMap = args.colorUser ?: ""
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}
