package com.saulhervas.easychat.ui.new_chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentNewChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewChatFragment : Fragment() {

    private lateinit var binding: FragmentNewChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewChatBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_new_chat, container, false)
    }

}