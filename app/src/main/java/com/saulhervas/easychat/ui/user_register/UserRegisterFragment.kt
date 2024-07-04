package com.saulhervas.easychat.ui.user_register

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.R
import com.saulhervas.easychat.data.model.modelsregister.RegisterRequest
import com.saulhervas.easychat.data.model.modelsregister.RegisterResponse
import com.saulhervas.easychat.data.repository.backend.retrofit.ApiClient
import com.saulhervas.easychat.data.repository.backend.retrofit.ApiService
import com.saulhervas.easychat.databinding.FragmentUserRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class UserRegisterFragment : Fragment() {

    private lateinit var binding: FragmentUserRegisterBinding
    private lateinit var apiService: ApiService
    private var isPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUserRegisterBinding.inflate(inflater, container, false)
        apiService = ApiClient.create(ApiService::class.java)

        setupListeners()
        return binding.root
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
            if (validatePasswords()) {
                val username = binding.etUser.text.toString()
                val password = binding.etPassword.text.toString()
                registerUser(username, password)
            } else {
                Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
        }
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

    private fun registerUser(username: String, password: String) {
        val registerRequest = RegisterRequest(username, password)
        val call = apiService.registerUser(registerRequest)
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Usuario creado con éxito", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_userRegister_to_userLogin)
                } else {
                    Toast.makeText(context, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(context, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun validatePasswords(): Boolean {
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etPasswordRepeat.text.toString()
        return password == confirmPassword
    }

}



