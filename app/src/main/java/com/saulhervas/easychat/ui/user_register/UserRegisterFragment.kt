package com.saulhervas.easychat.ui.user_register

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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
import android.widget.Toast
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
    private var isPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserRegisterBinding.inflate(inflater, container, false)
        setupListeners()
        setOnCLickListener()
        setupUI(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModels()
    }

    private fun observeViewModels(){
        lifecycleScope.launch {
            viewModel.loadingState.collect { visibility ->
                //binding.progressBar.visibility = if (visibility) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.registerUserState.collect { isRegister ->
                if (isRegister) {
                    Toast.makeText(context, "Usuario creado con éxito", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_userRegister_to_userLogin)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.errorState.collect { errorMsg ->
                errorMsg?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    viewModel.clearError() // Limpiar el error después de mostrarlo
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListeners() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No necesitas hacer nada aquí
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No necesitas hacer nada aquí
            }

            override fun afterTextChanged(s: Editable?) {
                validatePasswords()
            }
        }

        binding.etPassword.addTextChangedListener(textWatcher)
        binding.etPasswordRepeat.addTextChangedListener(textWatcher)

        binding.etPassword.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.etPassword.right - binding.etPassword.compoundDrawables[2].bounds.width())) {
                    togglePasswordVisibility(binding.etPassword)
                    return@setOnTouchListener true
                }
            }
            false
        }

        binding.etPasswordRepeat.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.etPasswordRepeat.right - binding.etPasswordRepeat.compoundDrawables[2].bounds.width())) {
                    togglePasswordVisibility(binding.etPasswordRepeat)
                    return@setOnTouchListener true
                }
            }
            false
        }

        binding.btnRegister.setOnClickListener {
            if (validateFields()) {
                if (validatePasswords()) {
                    val username = binding.etUser.text.toString()
                    val password = binding.etPassword.text.toString()
                    viewModel.registerUser(username, password)
                } else {
                    Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setOnCLickListener() {
        binding.imageButton.setOnClickListener {
            findNavController().navigate(R.id.action_userRegister_to_userLogin)
        }

        binding.tvRecoverPass.setOnClickListener {
            findNavController().navigate(R.id.action_userRegister_to_userRecoverPass)
        }

        binding.tvSession.setOnClickListener {
            findNavController().navigate(R.id.action_userRegister_to_userLogin)
        }
    }

    private fun validateFields(): Boolean {
        return binding.etUser.text.isNotEmpty() &&
                binding.etPassword.text.isNotEmpty() &&
                binding.etPasswordRepeat.text.isNotEmpty()
    }

    private fun togglePasswordVisibility(editText: EditText) {
        if (editText.transformationMethod is PasswordTransformationMethod) {
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            editText.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_password_visibility,
                0
            )
        } else {
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            editText.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_password_visibility,
                0
            )
        }
        editText.setSelection(editText.text.length)  // Move the cursor to the end
    }

    private fun validatePasswords(): Boolean {
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etPasswordRepeat.text.toString()
        return password == confirmPassword
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







