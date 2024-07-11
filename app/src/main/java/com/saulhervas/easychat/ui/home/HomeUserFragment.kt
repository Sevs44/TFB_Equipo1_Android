package com.saulhervas.easychat.ui.home

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
import com.qualentum.sprint3.main.ui.list.OpenChatAdapter
import com.saulhervas.easychat.databinding.FragmentHomeUserBinding
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeUserFragment : Fragment() {
    private lateinit var binding: FragmentHomeUserBinding
    private val viewModel: HomeViewModel by viewModels()
    private val args: HomeUserFragmentArgs by navArgs()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        observeViewModel()
    }

    private fun setUpViewModel() {
        lifecycleScope.launch {
            viewModel.getOpenChats(token)
        }
    }

    private fun getUserArgs() {
        token = args.token
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

    private fun setUpRecyclerView(itemList: ArrayList<OpenChatItemModel>) {
        binding.rvChats.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChats.adapter =
            OpenChatAdapter(itemList) { changeScreen() }
    }

    private fun changeScreen() {
        val action = HomeUserFragmentDirections.actionHomeUserToChatLog()
        findNavController().navigate(action)
    }
}