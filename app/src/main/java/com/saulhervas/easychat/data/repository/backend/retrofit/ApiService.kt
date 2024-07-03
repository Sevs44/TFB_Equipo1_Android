package com.saulhervas.easychat.data.repository.backend.retrofit

import com.saulhervas.easychat.data.model.modelslogin.LoginRequest
import com.saulhervas.easychat.data.model.modelslogin.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("users/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>
}