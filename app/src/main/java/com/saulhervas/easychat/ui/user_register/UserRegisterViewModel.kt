package com.saulhervas.easychat.ui.user_register

import androidx.lifecycle.ViewModel
import com.saulhervas.easychat.data.repository.backend.retrofit.ApiClient
import com.saulhervas.easychat.data.repository.backend.retrofit.ApiService
import com.saulhervas.easychat.data.repository.response.register.RegisterRequest
import com.saulhervas.easychat.data.repository.response.register.RegisterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserRegisterViewModel @Inject constructor(

): ViewModel() {
    private val loadingMutableState = MutableStateFlow(true)
    val loadingState: StateFlow<Boolean> = loadingMutableState

    private val errorMutableState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = errorMutableState

    private val registerUserMutableState = MutableStateFlow<Boolean>(false)
    val registerUserState: StateFlow<Boolean> = registerUserMutableState

    private var apiService: ApiService = ApiClient.create(ApiService::class.java)
    fun registerUser(username: String, password: String) {
        val registerRequest = RegisterRequest(username, password)
        val call = apiService.registerUser(registerRequest)
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    registerUserMutableState.value = true
                } else {
                    errorMutableState.value = "Error al crear el usuario"
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                errorMutableState.value = "Error al crear el usuario"
            }
        })
    }

    fun clearError() {
        errorMutableState.value = null
    }

}