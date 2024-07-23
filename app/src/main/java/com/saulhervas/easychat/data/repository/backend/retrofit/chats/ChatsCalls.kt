package com.saulhervas.easychat.data.repository.backend.retrofit.chats

import android.content.Context
import com.saulhervas.easychat.data.repository.backend.retrofit.ApiService
import com.saulhervas.easychat.data.repository.backend.retrofit.BaseService
import com.saulhervas.easychat.data.repository.response.chats.OpenChatsResponse
import com.saulhervas.easychat.data.repository.response.new_chat.NewChatRequest
import com.saulhervas.easychat.data.repository.response.new_chat.NewChatResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import javax.inject.Inject

class ChatsCalls @Inject constructor(
    private val apiService: ApiService,
    //val context: Context
) : BaseService() {
    suspend fun callOpenChats(): BaseResponse<OpenChatsResponse> {
        return apiCall { apiService.getOpenChats() }
    }
    suspend fun callNewChat(
        newChatRequest: NewChatRequest
    ): BaseResponse<NewChatResponse> {
        return apiCall { apiService.postNewChat(newChatRequest) }
    }
}