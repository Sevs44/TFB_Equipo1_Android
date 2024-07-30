package com.saulhervas.easychat.data.repository.backend.retrofit

import com.saulhervas.easychat.data.repository.response.chats.OpenChatsResponse
import com.saulhervas.easychat.data.repository.response.delete_chat.DeleteChatResponse
import com.saulhervas.easychat.data.repository.response.login.LoginRequest
import com.saulhervas.easychat.data.repository.response.login.LoginResponse
import com.saulhervas.easychat.data.repository.response.logout.LogoutResponse
import com.saulhervas.easychat.data.repository.response.messages_list.MessagesListResponse
import com.saulhervas.easychat.data.repository.response.new_chat.NewChatRequest
import com.saulhervas.easychat.data.repository.response.new_chat.NewChatResponse
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageRequest
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageResponse
import com.saulhervas.easychat.data.repository.response.profile.UserProfileResponse
import com.saulhervas.easychat.data.repository.response.register.RegisterRequest
import com.saulhervas.easychat.data.repository.response.register.RegisterResponse
import com.saulhervas.easychat.data.repository.response.user_list.UserListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users/profile")
    suspend fun getProfile(): Response<UserProfileResponse>

    @GET("users")
    suspend fun getUserList(): Response<UserListResponse>

    @GET("chats/view")
    suspend fun getOpenChats(): Response<OpenChatsResponse>

    @GET("messages/list/{id}")
    suspend fun getMessagesList(
        @Path("id") id: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<MessagesListResponse>

    @POST("messages/new")
    suspend fun newMessage(@Body newMessageRequest: NewMessageRequest): Response<NewMessageResponse>

    @POST("users/logout")
    suspend fun postLogoutUser(): Response<LogoutResponse>

    @POST("users/biometric")
    suspend fun postBiometric(): Response<LoginResponse>

    @POST("chats")
    suspend fun postNewChat(
        @Body newChatRequest: NewChatRequest
    ): Response<NewChatResponse>

    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("users/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @DELETE("chats/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<DeleteChatResponse>
}