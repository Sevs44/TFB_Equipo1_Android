package com.saulhervas.easychat.data.repository.backend.retrofit

import com.saulhervas.easychat.data.repository.response.chats.OpenChatsResponse
import com.saulhervas.easychat.data.repository.response.login.LoginRequest
import com.saulhervas.easychat.data.repository.response.login.LoginResponse
import com.saulhervas.easychat.data.repository.response.logout.LogoutResponse
import com.saulhervas.easychat.data.repository.response.register.RegisterRequest
import com.saulhervas.easychat.data.repository.response.register.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("users/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @GET("chats/view")
    suspend fun getOpenChats(@Header("Authorization") token: String): Response<OpenChatsResponse>

    @POST("users/logout")
    suspend fun postLogoutUser(@Header("Authorization") token: String): Response<LogoutResponse>
}