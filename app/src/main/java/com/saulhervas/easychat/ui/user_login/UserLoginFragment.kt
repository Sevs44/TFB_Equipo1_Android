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
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.R
import com.saulhervas.easychat.data.model.modelslogin.LoginRequest
import com.saulhervas.easychat.data.model.modelslogin.LoginResponse
import com.saulhervas.easychat.data.repository.backend.retrofit.ApiClient
import com.saulhervas.easychat.data.repository.backend.retrofit.ApiService
import com.saulhervas.easychat.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class UserLoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        apiService = ApiClient.create(ApiService::class.java)

        setOnClickListener()
        setupUI(binding.root)

        return binding.root
    }

    private fun setOnClickListener() {
        binding.btnLogin.setOnClickListener {
            loginUser(binding.etUser.text.toString(), binding.etPassword.text.toString())
        }
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_userLogin_to_userRegister)

        }
        binding.tvRecoverPass.setOnClickListener {
            findNavController().navigate(R.id.action_userLogin_to_userRecoverPass)
        }
    }

    private fun loginUser(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)
        val call = apiService.loginUser(loginRequest)
        Log.d(TAG, "onResponse: $call")
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d(TAG, "onResponse: $response")
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        findNavController().navigate(R.id.action_userLogin_to_homeUser)
                        Toast.makeText(
                            requireContext(),
                            "Login exitoso: ${loginResponse.user.nick}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    val errorResponse = ApiClient.parseError(response)
                    if (errorResponse != null) {
                        Toast.makeText(
                            requireContext(),
                            "@string/error" + " ${errorResponse.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(requireContext(), "@string/error", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Error de red , intentelo mas tarde",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })
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

