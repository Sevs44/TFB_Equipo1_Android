package com.saulhervas.easychat.data.repository.backend.retrofit.chats

import com.saulhervas.easychat.data.repository.backend.retrofit.ApiService
import com.saulhervas.easychat.data.repository.backend.retrofit.BaseService
import com.saulhervas.easychat.data.repository.response.chats.OpenChatsResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import javax.inject.Inject

class ChatsCalls @Inject constructor(
    private val apiService: ApiService
) : BaseService() {
    suspend fun callOpenChats(): BaseResponse<OpenChatsResponse> {
        return apiCall { apiService.getOpenChats() }
    }
}