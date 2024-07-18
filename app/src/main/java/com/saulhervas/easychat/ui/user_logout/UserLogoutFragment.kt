package com.saulhervas.easychat.ui.user_logout

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.databinding.FragmentUserLogoutBinding
import com.saulhervas.easychat.domain.encryptedsharedpreference.SecurePreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserLogoutFragment : Fragment() {

    private lateinit var binding: FragmentUserLogoutBinding
    private val userLoginViewModel: UserLogoutViewModel by viewModels()
    private lateinit var token: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUserLogoutBinding.inflate(inflater, container, false)
        token = SecurePreferences.getBiometricToken(requireContext()).toString()

        setOnClickListener()
        return binding.root
    }

    private fun setOnClickListener() {
        binding.btnCloseSessionDefinitive.setOnClickListener {
            Log.d(TAG, "boton logout ${token}")
            userLoginViewModel.logoutUser(token)
            observeViewModel()
        }
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