package com.saulhervas.easychat.ui.user_register

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentUserRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserRegisterFragment : Fragment() {

    private lateinit var binding: FragmentUserRegisterBinding
    private val viewModel: UserRegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserRegisterBinding.inflate(inflater, container, false)
        setupListeners()
        setOnClickListener()
        setupUI(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModels()
    }

    private fun observeViewModels() {
        lifecycleScope.launch {
            viewModel.loadingState.collect { isLoading ->
                showProgressBar(isLoading)
            }
        }

        lifecycleScope.launch {
            viewModel.registerUserState.collect { isRegistered ->
                if (isRegistered) {
                    showAlertDialog(
                        getString(R.string.user_register),
                        getString(R.string.user_success)
                    ) {
                        findNavController().navigate(R.id.action_userRegister_to_dashboardFirstPageFragment)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.errorState.collect { errorMsg ->
                errorMsg?.let {
                    when {
                        it.contains("401") -> {
                            showAlertDialog(
                                getString(R.string.error),
                                getString(R.string.user_already_exists)
                            )
                        }

                        else -> {
                            showAlertDialog(getString(R.string.error), it)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.passwordVisibilityState.collect { isVisible ->
                binding.etPassword.transformationMethod =
                    if (isVisible) HideReturnsTransformationMethod.getInstance()
                    else PasswordTransformationMethod.getInstance()
                binding.etPassword.setSelection(binding.etPassword.text.length)
            }
        }

        lifecycleScope.launch {
            viewModel.passwordRepeatVisibilityState.collect { isVisible ->
                binding.etPasswordRepeat.transformationMethod =
                    if (isVisible) HideReturnsTransformationMethod.getInstance()
                    else PasswordTransformationMethod.getInstance()
                binding.etPasswordRepeat.setSelection(binding.etPasswordRepeat.text.length)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListeners() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validatePasswords()
            }
        }

        binding.etPassword.addTextChangedListener(textWatcher)
        binding.etPasswordRepeat.addTextChangedListener(textWatcher)

        binding.etPassword.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.etPassword.right - binding.etPassword.compoundDrawables[2].bounds.width())) {
                    viewModel.togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }

        binding.etPasswordRepeat.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.etPasswordRepeat.right - binding.etPasswordRepeat.compoundDrawables[2].bounds.width())) {
                    viewModel.togglePasswordRepeatVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun setOnClickListener() {
        binding.imageButton.setOnClickListener {
            findNavController().navigate(R.id.action_userRegister_to_userLogin)
        }

        binding.tvSession.setOnClickListener {
            findNavController().navigate(R.id.action_userRegister_to_userLogin)
        }

        binding.btnRegister.setOnClickListener {
            val username = binding.etUser.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etPasswordRepeat.text.toString()
            val nick = binding.etNick.text.toString()

            val isUsernameEmpty = username.isEmpty()
            val isPasswordEmpty = password.isEmpty()
            val isConfirmPasswordEmpty = confirmPassword.isEmpty()
            val isNickEmpty = nick.isEmpty()

            updateEditTextUI(binding.etUser, isUsernameEmpty)
            updateEditTextUI(binding.etPassword, isPasswordEmpty)
            updateEditTextUI(binding.etPasswordRepeat, isConfirmPasswordEmpty)
            updateEditTextUI(binding.etNick, isNickEmpty)

            when {
                isUsernameEmpty || isPasswordEmpty || isConfirmPasswordEmpty || isNickEmpty -> {
                    showAlertDialog(
                        getString(R.string.error),
                        getString(R.string.user_register_error)
                    )
                }

                !validatePasswords() -> {
                    updatePasswordEditTextUI(true)
                    showAlertDialog(
                        getString(R.string.error),
                        getString(R.string.error_password_mismatch)
                    )
                }

                else -> {
                    showProgressBar(true)
                    viewModel.registerUser(username, password, nick)
                }
            }
        }

    }

    private fun showAlertDialog(
        title: String,
        message: String,
        onDismiss: (() -> Unit)? = null
    ) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setCancelable(true)
            .show()

        Handler(Looper.getMainLooper()).postDelayed({
            if (alertDialog.isShowing) {
                alertDialog.dismiss()
                onDismiss?.invoke()
            }
        }, 2500)
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

    private fun updatePasswordEditTextUI(isMismatch: Boolean) {
        if (isMismatch) {
            binding.etPassword.setHintTextColor(Color.RED)
            binding.etPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background_error)
            binding.etPasswordRepeat.setHintTextColor(Color.RED)
            binding.etPasswordRepeat.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background_error)
        } else {
            binding.etPassword.setHintTextColor(Color.GRAY)
            binding.etPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background)
            binding.etPasswordRepeat.setHintTextColor(Color.GRAY)
            binding.etPasswordRepeat.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background)
        }
    }

    private fun validatePasswords(): Boolean {
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etPasswordRepeat.text.toString()
        return password == confirmPassword
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

    private fun showProgressBar(show: Boolean) {
        if (show) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}