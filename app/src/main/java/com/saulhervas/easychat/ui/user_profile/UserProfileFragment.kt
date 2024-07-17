package com.saulhervas.easychat.ui.user_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.saulhervas.easychat.databinding.FragmentUserConfigBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private lateinit var binding: FragmentUserConfigBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserConfigBinding.inflate(inflater, container, false)
        setOnClickListeners()
        return binding.root
    }

    private fun setOnClickListeners() {

    }

}