package com.saulhervas.easychat.ui.user_login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentLoginBinding
import com.saulhervas.easychat.domain.encryptedsharedpreference.SecurePreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

@AndroidEntryPoint
class UserLoginFragment : Fragment() {

    private val userLoginViewModel: UserLoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var executor: Executor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupBiometricAuthentication()
        setOnClickListener()
        setupUI(binding.root)

        return binding.root
    }

    private fun setupBiometricAuthentication() {
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        requireContext(),
                        "Authentication error: $errString",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val biometricToken = SecurePreferences.getBiometricToken(requireContext())
                    if (biometricToken != null) {
                        userLoginViewModel.loginWithBiometrics()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "No biometric token found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT)
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for EasyChat")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        binding.tvBiometric.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }
    private fun setOnClickListener() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUser.text.toString()
            val password = binding.etPassword.text.toString()
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Enter username and password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                userLoginViewModel.loginUser(username, password)
            }
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
                SecurePreferences.saveBiometricToken(requireContext(), it.token)
                val action =
                    UserLoginFragmentDirections.actionUserLoginToHomeUser(it.userLogin.id)
                findNavController().navigate(action)
                Toast.makeText(
                    requireContext(),
                    "$it.token",
                    Toast.LENGTH_LONG
                ).show()
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
