package com.saulhervas.easychat.data.repository.backend.retrofit

import com.saulhervas.easychat.data.repository.response.chats.OpenChatsResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import javax.inject.Inject

class BaseApiService @Inject constructor(
    val apiService: ApiService
) : BaseService() {
    suspend fun callOpenChats(): BaseResponse<OpenChatsResponse> {
        return apiCall { apiService.getOpenChats() }
    }
}