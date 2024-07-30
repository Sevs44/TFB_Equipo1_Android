package com.saulhervas.easychat.ui.new_chat

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.databinding.FragmentNewChatBinding
import com.saulhervas.easychat.domain.model.UserNewChatItemModel
import com.saulhervas.easychat.domain.model.UserSession
import com.saulhervas.easychat.ui.home.HomeViewModel
import com.saulhervas.easychat.ui.new_chat.new_chat_list.NewChatAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewChatFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentNewChatBinding
    private lateinit var newChatAdapter: NewChatAdapter
    private var allChats: MutableList<UserNewChatItemModel> = mutableListOf()

    @Inject
    lateinit var userSession: UserSession

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewChatBinding.inflate(inflater, container, false)
        setOnClickListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setUpAlphabetScroller()
    }

    private fun setOnClickListener() {
        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeViewModel() {
        updateLists()
        lifecycleScope.launch {
            viewModel.newChatsState.collect {
                allChats = it
                setUpRecyclerView(it)
            }
        }
        lifecycleScope.launch {

        }
    }

    private fun updateLists() {
        viewModel.getOpenChats()
        viewModel.getUserList()
        viewModel.filterUsers(userSession.id)
    }

    private fun openChatCreated(chat: UserNewChatItemModel, idChat: String) {
        Log.d("openChatCreated", "openChatCreated: $chat")
        changeScreen(chat, idChat)
    }

    private fun setUpRecyclerView(userList: MutableList<UserNewChatItemModel>) {
        newChatAdapter = NewChatAdapter(userList, viewModel.colorMap) { user ->
            Log.d("el usuario que clico", "setUpRecyclerView: $user")
            showCreateChatDialog(user)
        }
        binding.rvUsersNewChat.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsersNewChat.adapter = newChatAdapter

        binding.swChat.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterUsers(newText.orEmpty())
                return true
            }

        })

    }

    private fun filterUsers(query: String) {
        val filteredUsers = if (query.isEmpty()) {
            allChats
        } else {
            allChats.filter { it.nick?.contains(query, ignoreCase = true) ?: false }
        }

        newChatAdapter.updateItems(filteredUsers)
    }

    private fun changeScreen(openChatItemModel: UserNewChatItemModel?, idChat: String) {
        Log.d("changeScreen", "changeScreen: $openChatItemModel")
        lifecycleScope.launch {
            delay(300)
            val action = NewChatFragmentDirections.actionNewChatFragmentToChatLog(
                idChat,
                openChatItemModel?.nick.toString(),
                openChatItemModel?.onlineStatus ?: true
            )
            findNavController().navigate(action)
        }
    }

    private fun showCreateChatDialog(newChatItem: UserNewChatItemModel?) {
        val message = "Crear Chat"
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
            .setPositiveButton("Yes") { dialog, _ ->
                Log.d("newChatItem", "YES: $newChatItem")
                if (newChatItem != null) {
                    newChatItem.id?.let {
                        viewModel.createChat(
                            userSession.id,
                            idTarget = it
                        )
                    }
                    lifecycleScope.launch {
                        Log.d("newChatItem", "handleCreateChat: $newChatItem")
                        viewModel.chatCreatedSharedFlow.collect { chatCreated ->
                            Log.d("newChatItem", "handleCreateChateeeeeeeeeeeeeeee: $chatCreated")
                            if (chatCreated.success) {
                                viewModel.openChatsState.collect {
                                    Log.d("newChatItem", "handleCreateChatasd: $it")
                                    Log.d("newChatItem", "handleCreateChat: $newChatItem")
                                    openChatCreated(newChatItem, chatCreated.chat.id)
                                }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Chat no creado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun setUpAlphabetScroller() {
        val letters = ('A'..'Z')
        for (letter in letters) {
            val textView = TextView(requireContext()).apply {
                text = letter.toString()
                textSize = 16f
                setPadding(8, 8, 8, 8)
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
                setOnClickListener {
                    scrollToLetter(letter, this)
                }
            }
            binding.alphabetScroller.addView(textView)
        }
    }

    private fun scrollToLetter(letter: Char, tv: TextView) {
        val position = newChatAdapter.getPositionForSection(letter)
        if (position >= 0) {
            highlightLetter(tv, Color.CYAN)
            val smoothScroller: RecyclerView.SmoothScroller =
                object : LinearSmoothScroller(requireContext()) {
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_START
                    }
                }
            smoothScroller.targetPosition = position
            binding.rvUsersNewChat.layoutManager?.startSmoothScroll(smoothScroller)
        } else {
            highlightLetter(tv, Color.RED)
        }
    }

    private fun highlightLetter(textView: TextView, color: Int) {
        val originalColor = textView.currentTextColor

        val colorAnimator = ValueAnimator.ofArgb(color, originalColor).apply {
            duration = 1000
            addUpdateListener { animator ->
                textView.setTextColor(animator.animatedValue as Int)
            }
            doOnEnd {
                textView.setTextColor(originalColor)
            }
        }

        textView.setTextColor(color)
        Handler(Looper.getMainLooper()).postDelayed({
            colorAnimator.start()
        }, 1000)
    }
}
