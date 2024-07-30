package com.saulhervas.easychat.ui.home

import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
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
import com.saulhervas.easychat.ui.home.open_chats_list.OpenChatAdapter
import com.saulhervas.easychat.utils.DebouncedOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeUserFragment : Fragment() {
    private lateinit var binding: FragmentHomeUserBinding
    private val viewModel: HomeViewModel by viewModels()
    private var imageUri: Uri? = null
    private var allChats: MutableList<OpenChatItemModel> = mutableListOf()
    private lateinit var chatAdapter: OpenChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeUserBinding.inflate(inflater, container, false)
        setUpStatusbar()
        setUpProfileBaseImage()
        setOnclickListener()
        return binding.root
    }

    private fun setUpStatusbar() {
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.color_app)
    }

    private fun setOnclickListener() {
        binding.btnAdd.setOnClickListener(object : DebouncedOnClickListener() {
            override fun onDebouncedClick(v: View) {
                val action = HomeUserFragmentDirections.actionHomeUserToNewChatFragment()
                findNavController().navigate(action)
            }
        })
        binding.imBtnSettings.setOnClickListener(object : DebouncedOnClickListener() {
            override fun onDebouncedClick(v: View) {
                val action = HomeUserFragmentDirections.actionHomeUserToUserConfig()
                findNavController().navigate(action)
            }
        })
        binding.ivProfile.setOnClickListener(object : DebouncedOnClickListener() {
            override fun onDebouncedClick(v: View) {
                val action = HomeUserFragmentDirections.actionHomeUserToPhotoEditFragment()
                findNavController().navigate(action)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        observeViewModel()
    }

    private fun setUpProfileBaseImage() {
        loadImageUri()
        if (binding.ivProfile.drawable == null) binding.ivProfile.setImageResource(R.drawable.usuario_1)
    }

    private fun loadImageUri() {
        SecurePreferences.getProfileImage(requireContext())?.let {
            binding.ivProfile.setImageURI(it)
            imageUri = it
        }
    }

    private fun setUpViewModel() {
        viewModel.getOpenChats()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.openChatsState.collect {
                Log.i("TAG", "observeViewModel: it $it")
                allChats = it
                setUpRecyclerView(allChats)
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

    private fun setUpRecyclerView(itemList: MutableList<OpenChatItemModel>) {
        chatAdapter = OpenChatAdapter(itemList, viewModel.colorMap) { chat ->
            showProgressBar(true)
            changeScreen(chat)
        }
        binding.rvChats.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChats.adapter = chatAdapter

        binding.swChat.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterUsers(newText.orEmpty())
                return true
            }
        })

        binding.rvChats.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)

            val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.bindingAdapterPosition
                    val idChat = chatAdapter.getIdChat(position)

                    val alertDialog = AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.alert_swiped))
                        .setMessage(getString(R.string.alert_swiped_message))
                        .setPositiveButton("Sí") { dialog, _ ->
                            lifecycleScope.launch {
                                try {
                                    if (idChat != null) {
                                        viewModel.deleteChats(idChat)
                                        allChats.removeAt(position)
                                        chatAdapter.notifyItemRemoved(position)
                                        checkAndShowBackgroundImage()
                                    }
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error deleting chat", e)
                                    e.printStackTrace()
                                    chatAdapter.notifyItemChanged(position)
                                }
                            }
                            dialog.dismiss()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            chatAdapter.notifyItemChanged(position)
                            dialog.dismiss()
                        }
                        .create()

                    alertDialog.show()
                }
            }

            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(this)
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
}

