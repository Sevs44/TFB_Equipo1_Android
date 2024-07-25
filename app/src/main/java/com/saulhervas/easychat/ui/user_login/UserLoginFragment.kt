package com.saulhervas.easychat.ui.user_login

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import kotlinx.coroutines.delay
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
            val userName = binding.etUser.text.toString()
            val password = binding.etPassword.text.toString()
            val isUsernameEmpty = userName.isEmpty()
            val isPasswordEmpty = password.isEmpty()

            updateEditTextUI(binding.etUser, isUsernameEmpty)
            updateEditTextUI(binding.etPassword, isPasswordEmpty)

            if (isUsernameEmpty || isPasswordEmpty) {
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.error))
                    .setMessage(getString(R.string.error_login))
                    .setCancelable(true)
                    .show()
                lifecycleScope.launch {
                    delay(2000)
                    alertDialog.dismiss()
                }
            } else {
                showProgressBar(true) // Mostrar el ProgressBar
                userLoginViewModel.loginUser(userName, password)
            }
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_userLogin_to_userRegister)
        }
    }

    private fun updateEditTextUI(editText: EditText, isEmpty: Boolean) {
        if (isEmpty) {
            editText.setHintTextColor(Color.RED)
            editText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background_error)
        } else {
            editText.setHintTextColor(Color.GRAY)
            editText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            userLoginViewModel.loginResult.collect {
                SecurePreferences.saveBiometricToken(requireContext(), it.token)
                showProgressBar(false)
                val action =
                    UserLoginFragmentDirections.actionUserLoginToHomeUser(it.token, it.userLogin.id)
                findNavController().navigate(action)
            }
        }

        lifecycleScope.launch {
            userLoginViewModel.loginResulterror.collect {
                showProgressBar(false)
                showErrorDialog()
            }
        }
    }

    private fun showErrorDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.error))
            .setMessage(getString(R.string.users_not_founds))
            .setCancelable(false)
            .create()

        dialog.show()
        Handler(Looper.getMainLooper()).postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }, 2000)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideKeyboard()
                false
            }
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                setupUI(view.getChildAt(i))
            }
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun showProgressBar(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}
