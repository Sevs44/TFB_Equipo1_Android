package com.saulhervas.easychat.data.repository.backend.retrofit.messages

import android.content.Context
import com.saulhervas.easychat.data.repository.backend.retrofit.ApiService
import com.saulhervas.easychat.data.repository.backend.retrofit.BaseService
import com.saulhervas.easychat.data.repository.response.messages_list.MessagesListResponse
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageRequest
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import javax.inject.Inject

class MessagesCalls @Inject constructor(
    private val apiService: ApiService,
    //val context: Context
) : BaseService() {
    suspend fun callMessageList(id: String, offset: Int, limit: Int): BaseResponse<MessagesListResponse> {
        return apiCall { apiService.getMessagesList(id, offset, limit) }
    }
    suspend fun newMessage(newMessageRequest: NewMessageRequest): BaseResponse<NewMessageResponse> {
        return apiCall { apiService.newMessage(newMessageRequest) }
    }
}