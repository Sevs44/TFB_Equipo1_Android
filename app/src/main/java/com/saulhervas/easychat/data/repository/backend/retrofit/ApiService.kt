package com.saulhervas.easychat.data.repository.backend.retrofit

import com.saulhervas.easychat.data.model.modelslogin.LoginRequest
import com.saulhervas.easychat.data.model.modelslogin.LoginResponse
import com.saulhervas.easychat.data.model.modelsregister.RegisterRequest
import com.saulhervas.easychat.data.model.modelsregister.RegisterResponse
import com.saulhervas.easychat.data.repository.response.chats.OpenChatsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @POST("users/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("users/register")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @Headers("Authorization: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY4OCIsImlhdCI6MTcyMDUyNjkwOSwiZXhwIjoxNzIzMTE4OTA5fQ.fy4ZoOJFLPIwH7Y7Gti4qgSTSoXw2Wqi_9FtNA5f8qM")
    @GET("chats/view")
    suspend fun getOpenChats(): Response<OpenChatsResponse>
}