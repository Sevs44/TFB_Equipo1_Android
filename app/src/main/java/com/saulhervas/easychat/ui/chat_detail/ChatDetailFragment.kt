package com.saulhervas.easychat.ui.chat_detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.saulhervas.easychat.databinding.FragmentChatDetailBinding
import com.saulhervas.easychat.domain.encryptedsharedpreference.SecurePreferences
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatDetailFragment : Fragment() {

    private lateinit var binding: FragmentChatDetailBinding
    private var imageUri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatDetailBinding.inflate(inflater, container, false)
        loadImageUri()
        requireContext()
        return binding.root
    }

    private fun loadImageUri() {
        SecurePreferences.getProfileImage(requireContext())?.let {
            binding.ivProfile.setImageURI(it)
            imageUri = it
        }
    }
}