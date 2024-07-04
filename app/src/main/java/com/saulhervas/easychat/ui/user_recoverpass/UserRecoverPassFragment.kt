package com.saulhervas.easychat.ui.user_recoverpass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentRecoverPassBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserRecoverPassFragment : Fragment() {

    private lateinit var binding: FragmentRecoverPassBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecoverPassBinding.inflate(inflater, container, false)
        onClickRegister(binding.root)
        return binding.root
    }

    fun onClickRegister(view: View) {
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_userRecoverPass_to_userRegister)
        }
    }


}