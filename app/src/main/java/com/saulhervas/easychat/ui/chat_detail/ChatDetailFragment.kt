package com.saulhervas.easychat.ui.chat_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.saulhervas.easychat.databinding.FragmentChatDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatDetailFragment : Fragment() {

    private lateinit var binding: FragmentChatDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

}