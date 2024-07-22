package com.saulhervas.easychat.ui.home

import android.net.Uri
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
import com.saulhervas.easychat.databinding.FragmentHomeUserBinding
import com.saulhervas.easychat.domain.encryptedsharedpreference.SecurePreferences
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import com.saulhervas.easychat.ui.home.list.OpenChatAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeUserFragment : Fragment() {
    private lateinit var binding: FragmentHomeUserBinding
    private val viewModel: HomeViewModel by viewModels()
    private val args: HomeUserFragmentArgs by navArgs()
    private lateinit var token: String
    private var imageUri: Uri? = null
    private lateinit var idUser: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeUserBinding.inflate(inflater, container, false)
        loadImageUri()
        setOnclickListener()
        return binding.root
    }

    fun setOnclickListener() {
        binding.btnAdd.setOnClickListener {
            val action = HomeUserFragmentDirections.actionHomeUserToNewChatFragment()
            findNavController().navigate(action)
        }
        binding.imBtnSettings.setOnClickListener {
            val action = HomeUserFragmentDirections.actionHomeUserToUserConfig(token, id.toString())
            findNavController().navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        observeViewModel()
    }

    private fun loadImageUri() {
        SecurePreferences.getProfileImage(requireContext())?.let {
            binding.ivProfile.setImageURI(it)
            imageUri = it
        }
    }


    private fun setUpViewModel() {
        lifecycleScope.launch {
            viewModel.getOpenChats(token)
        }
    }

    private fun getUserArgs() {
        token = args.token
        idUser = args.id
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.openChatsState.collect {
                Log.i("TAG", "observeViewModel: it $it")
                setUpRecyclerView(it)
            }
        }
        lifecycleScope.launch {
            viewModel.showImageBackgroundState.collect {
                showBackgroundImage(it)
            }
        }
    }

    private fun showBackgroundImage(show: Boolean) {
        if (!show) {
            binding.ivChat.visibility = View.GONE
            binding.tvTextChat.visibility = View.GONE
        } else {
            binding.ivChat.visibility = View.VISIBLE
            binding.tvTextChat.visibility = View.VISIBLE
        }
    }

    private fun setUpRecyclerView(itemList: MutableList<OpenChatItemModel>) {
        binding.rvChats.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChats.adapter =
            OpenChatAdapter(itemList) { chat -> changeScreen(chat) }
    }

    private fun changeScreen(openChatItemModel: OpenChatItemModel?) {
        val action = HomeUserFragmentDirections.actionHomeUserToChatLog(token, idUser, openChatItemModel?.id!!)
        findNavController().navigate(action)
    }

}