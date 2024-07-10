package com.saulhervas.easychat.data.repository.backend.retrofit

import com.saulhervas.easychat.data.model.modelslogin.LoginRequest
import com.saulhervas.easychat.data.model.modelslogin.LoginResponse
import com.saulhervas.easychat.data.model.modelsregister.RegisterRequest
import com.saulhervas.easychat.data.model.modelsregister.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse

    @POST("users/register")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>
}