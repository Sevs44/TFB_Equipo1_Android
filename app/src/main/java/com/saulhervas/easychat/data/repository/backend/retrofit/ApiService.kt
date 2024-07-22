package com.saulhervas.easychat.data.repository.backend.retrofit

import com.saulhervas.easychat.data.repository.response.chats.OpenChatsResponse
import com.saulhervas.easychat.data.repository.response.login.LoginRequest
import com.saulhervas.easychat.data.repository.response.login.LoginResponse
import com.saulhervas.easychat.data.repository.response.messages_list.MessagesListResponse
import com.saulhervas.easychat.data.repository.response.logout.LogoutResponse
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageRequest
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageResponse
import com.saulhervas.easychat.data.repository.response.register.RegisterRequest
import com.saulhervas.easychat.data.repository.response.register.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("users/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @GET("chats/view")
    suspend fun getOpenChats(): Response<OpenChatsResponse>

    @GET("messages/list/{id}")
    suspend fun getMessagesList(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<MessagesListResponse>

    @POST("messages/new")
    suspend fun newMessage(@Header("Authorization") token: String, @Body newMessageRequest: NewMessageRequest): Response<NewMessageResponse>

    @POST("users/logout")
    suspend fun postLogoutUser(@Header("Authorization") token: String): Response<LogoutResponse>

    @POST("users/biometric")
    suspend fun postBiometric(@Header("Authorization") token: String): Response<LoginResponse>
}