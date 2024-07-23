package com.saulhervas.easychat.data.repository.backend.retrofit.messages

import com.saulhervas.easychat.data.repository.backend.retrofit.ApiService
import com.saulhervas.easychat.data.repository.backend.retrofit.BaseService
import com.saulhervas.easychat.data.repository.response.messages_list.MessagesListResponse
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageRequest
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import javax.inject.Inject

class MessagesCalls @Inject constructor(
    private val apiService: ApiService
) : BaseService() {
    suspend fun callMessageList(token: String, id: String, offset: Int, limit: Int): BaseResponse<MessagesListResponse> {
        return apiCall { apiService.getMessagesList(token, id, offset, limit) }
    }
    suspend fun newMessage(token: String, newMessageRequest: NewMessageRequest): BaseResponse<NewMessageResponse> {
        return apiCall { apiService.newMessage(token, newMessageRequest) }
    }
}