package com.saulhervas.easychat.ui.user_login

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserLoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val userLoginViewModel: UserLoginViewModel by viewModels()
    var token: String = ""
    var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        setOnClickListener()
        setupUI(binding.root)
        return binding.root
    }

    private fun setOnClickListener() {
        binding.btnLogin.setOnClickListener {
            userLoginViewModel.loginUser(
                binding.etUser.text.toString(),
                binding.etPassword.text.toString()
                )
        }
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_userLogin_to_userRegister)
        }
        binding.tvRecoverPass.setOnClickListener {
            findNavController().navigate(R.id.action_userLogin_to_userRecoverPass)
        }
    }
    private fun observeViewModel() {
        lifecycleScope.launch {
            userLoginViewModel.loginResult.collect {
                Toast.makeText(
                    requireContext(),
                    "$it.token",
                    Toast.LENGTH_LONG
                ).show()
                Log.i(TAG, "observeViewModel: ${it.token} ${it.userLogin.id}")
                val action = UserLoginFragmentDirections.actionUserLoginToHomeUser(it.token, it.userLogin.id)
                findNavController().navigate(action)
            }
        }
        lifecycleScope.launch {
            userLoginViewModel.loginResulterror.collect {
                Toast.makeText(
                    requireContext(),
                    it,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI(view: View) {
        // Configurar listener para ocultar el teclado
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideKeyboard()
                false
            }
        }

        // Si una vista es un contenedor, repetir para sus hijos
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}
