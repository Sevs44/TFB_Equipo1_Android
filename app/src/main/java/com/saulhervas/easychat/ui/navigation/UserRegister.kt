package com.saulhervas.easychat.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentUserRegisterBinding


class UserRegister : Fragment() {


    private lateinit var binding: FragmentUserRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUserRegisterBinding.inflate(inflater, container, false)

        binding.imageButton.setOnClickListener {
            findNavController().navigate(R.id.action_userRegister_to_userLogin)
        }
        onClickSession(binding.root)
        onClickRecoverPass(binding.root)
        return binding.root
    }

    fun onClickSession(view: View) {
        binding.tvSession.setOnClickListener {
            findNavController().navigate(R.id.action_userRegister_to_userLogin)
        }
    }

    fun onClickRecoverPass(view: View) {
        binding.tvRecoverPass.setOnClickListener {
            findNavController().navigate(R.id.action_userRegister_to_userRecoverPass)
        }
    }

}