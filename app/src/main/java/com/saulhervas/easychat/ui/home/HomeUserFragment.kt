package com.saulhervas.easychat.ui.home

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentHomeUserBinding
import com.saulhervas.easychat.domain.encryptedsharedpreference.SecurePreferences
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import com.saulhervas.easychat.domain.model.UserSession
import com.saulhervas.easychat.ui.home.open_chats_list.OpenChatAdapter
import com.saulhervas.easychat.utils.DebouncedOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeUserFragment @Inject constructor() : Fragment() {

    private lateinit var binding: FragmentHomeUserBinding
    private val viewModel: HomeViewModel by viewModels()
    private var imageUri: Uri? = null

    @Inject
    lateinit var userSession: UserSession
    private var allChats: MutableList<OpenChatItemModel> = mutableListOf()
    private lateinit var chatAdapter: OpenChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeUserBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        setupStatusBar()
        setupProfileImage()
        setupClickListeners()
    }

    private fun setupStatusBar() {
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.color_app)
    }

    private fun setupProfileImage() {
        loadImageUri()
        if (binding.ivProfile.drawable == null) {
            binding.ivProfile.setImageResource(R.drawable.usuario_1)
        }
    }

    private fun loadImageUri() {
        SecurePreferences.getProfileImage(requireContext())?.let {
            binding.ivProfile.setImageURI(it)
            imageUri = it
        }
    }

    private fun setupClickListeners() {
        binding.btnAdd.setOnClickListener(createDebouncedClickListener {
            navigateToNewChat()
        })
        binding.imBtnSettings.setOnClickListener(createDebouncedClickListener {
            navigateToUserConfig()
        })
        binding.ivProfile.setOnClickListener(createDebouncedClickListener {
            navigateToPhotoEdit()
        })
    }

    private fun createDebouncedClickListener(action: () -> Unit) =
        object : DebouncedOnClickListener() {
            override fun onDebouncedClick(v: View) {
                action()
            }
        }

    private fun navigateToNewChat() {
        val action = HomeUserFragmentDirections.actionHomeUserToNewChatFragment()
        findNavController().navigate(action)
    }

    private fun navigateToUserConfig() {
        val action = HomeUserFragmentDirections.actionHomeUserToUserConfig()
        findNavController().navigate(action)
    }

    private fun navigateToPhotoEdit() {
        val action = HomeUserFragmentDirections.actionHomeUserToPhotoEditFragment()
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        observeViewModel()
    }

    private fun setupViewModel() {
        viewModel.getOpenChats()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.openChatsState.collect {
                allChats = it
                setupRecyclerView(allChats)
            }
        }
        lifecycleScope.launch {
            viewModel.showImageBackgroundState.collect {
                showBackgroundImage(it)
            }
        }
        lifecycleScope.launch {
            viewModel.loadingState.collect { isLoading ->
                showProgressBar(isLoading)
            }
        }
    }

    private fun showBackgroundImage(show: Boolean) {
        binding.ivChat.visibility = if (show) View.VISIBLE else View.GONE
        binding.tvTextChat.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun setupRecyclerView(itemList: MutableList<OpenChatItemModel>) {
        chatAdapter = OpenChatAdapter(itemList, viewModel.colorMap) { chat ->
            showProgressBar(true)
            changeScreen(chat)
        }
        binding.rvChats.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChats.adapter = chatAdapter

        setupSearchView()
        setupSwipeToDelete()
    }

    private fun setupSearchView() {
        binding.swChat.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterUsers(newText.orEmpty())
                return true
            }
        })
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                handleSwipe(viewHolder.bindingAdapterPosition)
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvChats)
    }

    private fun handleSwipe(position: Int) {
        val idChat = chatAdapter.getIdChat(position)
        Log.e("TAG", "Chat deletedaaaaaaaaaaaaaaaaaa: $idChat")
        val idSource = chatAdapter.getIdSource(position)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_swiped))
            .setMessage(getString(R.string.alert_swiped_message))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                handleDeleteChat(position, idChat, idSource)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                chatAdapter.notifyItemChanged(position)
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    private fun handleDeleteChat(position: Int, idChat: String?, idSource: String?) {
        lifecycleScope.launch {
            try {
                if (idChat != null) {
                    if (userSession.id == idSource) {
                        viewModel.deleteChats(idChat)
                        allChats.removeAt(position)
                        chatAdapter.notifyItemRemoved(position)
                        checkAndShowBackgroundImage()
                    } else {
                        showAlertDialog(
                            getString(R.string.noDeleteChat),
                            getString(R.string.noDeleteChatExplication)
                        )
                        chatAdapter.notifyItemChanged(position)
                    }
                } else {
                    Log.e("TAG", "Error deleting chat")
                }
            } catch (e: Exception) {
                Log.e("TAG", "Error deleting chat", e)
                chatAdapter.notifyItemChanged(position)
            }
        }
    }

    private fun changeScreen(openChatItemModel: OpenChatItemModel?) {
        lifecycleScope.launch {
            delay(300)
            val action = HomeUserFragmentDirections.actionHomeUserToChatLog(
                openChatItemModel?.idChat.toString(),
                openChatItemModel?.nickTargetUser.toString(),
                openChatItemModel?.isOnlineUser ?: true
            )
            findNavController().navigate(action)
            showProgressBar(false)
        }
    }

    private fun filterUsers(query: String) {
        val filteredUsers = if (query.isEmpty()) {
            allChats
        } else {
            allChats.filter { it.nickTargetUser?.contains(query, ignoreCase = true) == true }
        }
        chatAdapter.updateList(filteredUsers)
    }

    private fun showProgressBar(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun checkAndShowBackgroundImage() {
        showBackgroundImage(allChats.isEmpty())
    }
    private fun showAlertDialog(title: String, message: String, onDismiss: (() -> Unit)? = null) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setCancelable(true)
            .show()

        Handler(Looper.getMainLooper()).postDelayed({
            if (alertDialog.isShowing) {
                alertDialog.dismiss()
                onDismiss?.invoke()
            }
        }, 2000)
    }
}
