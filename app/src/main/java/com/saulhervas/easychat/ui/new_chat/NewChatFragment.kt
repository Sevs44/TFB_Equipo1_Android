package com.saulhervas.easychat.ui.new_chat

import android.os.Bundle
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
                setUpRecyclerView(viewModel.getFilteredList(it))
            }
        }
    }

    private fun updateLists() {
        viewModel.getUserList()
        viewModel.filterUsers(userSession.id)
    }

    private fun openChatCreated(chat: UserNewChatItemModel, idChat: String) {
        Log.d("openChatCreated", "openChatCreated: $chat")
        changeScreen(chat, idChat)
    }

    private fun setUpRecyclerView(userList: List<UserNewChatItemModel>) {
        newChatAdapter = NewChatAdapter(requireContext(), userList, viewModel.colorMap) { user ->
            showCreateChatDialog(user)
        }
        binding.rvUsersNewChat.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsersNewChat.adapter = newChatAdapter

        binding.swChat.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterUsers(newText.orEmpty(), userSession.id)
                return true
            }

        })

    }

    private fun filterUsers(query: String, currentUserId: String) {
        val filteredUsers = if (query.isEmpty()) {
            allChats.filter { it.id != currentUserId }
        } else {
            allChats.filter {
                it.id != currentUserId && (it.nick?.contains(query, ignoreCase = true) ?: false)
            }
        }
        newChatAdapter.updateList(filteredUsers)
    }

    private fun changeScreen(openChatItemModel: UserNewChatItemModel?, idChat: String) {
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
                if (newChatItem != null) {
                    newChatItem.id?.let {
                        viewModel.createChat(
                            userSession.id,
                            idTarget = it
                        )
                    }
                    lifecycleScope.launch {
                        viewModel.chatCreatedSharedFlow.collect { chatCreated ->

                        if (chatCreated.success) {
                                viewModel.openChatsState.collect {
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
            }
            binding.alphabetScroller.addView(textView)
        }
    }
}
