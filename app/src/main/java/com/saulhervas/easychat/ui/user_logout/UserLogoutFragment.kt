package com.saulhervas.easychat.ui.user_logout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.saulhervas.easychat.databinding.FragmentUserLogoutBinding
import com.saulhervas.easychat.ui.home.HomeUserFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserLogoutFragment : Fragment() {

    private lateinit var binding: FragmentUserLogoutBinding
    private val userLoginViewModel: UserLogoutViewModel by viewModels()
    private val args: HomeUserFragmentArgs by navArgs()
    private lateinit var token: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUserLogoutBinding.inflate(inflater, container, false)
        getUserArgs()
        setOnClickListener()
        return binding.root
    }

    private fun setOnClickListener() {
        binding.btnCloseSession.setOnClickListener {
            userLoginViewModel.logoutUser(token)
            observeViewModel()
        }
    }

    private fun getUserArgs() {
        token = args.token
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            userLoginViewModel.logoutResult.collect {
                val action =
                    UserLogoutFragmentDirections.actionUserLogoutFragmentToUserLogin()
                findNavController().navigate(action)
                Toast.makeText(
                    requireContext(),
                    "User logged out",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        lifecycleScope.launch {
            userLoginViewModel.logoutResulterror.collect {
                Toast.makeText(
                    requireContext(),
                    it,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}