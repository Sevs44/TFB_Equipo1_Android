package com.saulhervas.easychat.ui.user_logout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.saulhervas.easychat.databinding.FragmentUserLogoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserLogoutFragment : Fragment() {

    private lateinit var binding: FragmentUserLogoutBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUserLogoutBinding.inflate(inflater, container, false)
        return binding.root
    }

}