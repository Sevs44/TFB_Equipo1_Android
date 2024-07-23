package com.saulhervas.easychat.domain.usecases

import com.saulhervas.easychat.data.repository.backend.retrofit.messages.MessagesDataProvider
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageRequest
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.messages_list.MessagesModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageUseCases @Inject constructor(
    private val dataProvider: MessagesDataProvider
) {
    fun getMessagesList(token: String, id: String, offset: Int, limit: Int): Flow<BaseResponse<MessagesModel>> {
        return dataProvider.getMessagesList(token, id, offset, limit)
    }
    fun newMessage(token: String, newMessageRequest: NewMessageRequest): Flow<BaseResponse<NewMessageResponse>> {
        return dataProvider.newMessage(token, newMessageRequest)
    }
}