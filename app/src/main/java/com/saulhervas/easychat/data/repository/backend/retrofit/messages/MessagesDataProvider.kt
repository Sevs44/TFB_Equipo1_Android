package com.saulhervas.easychat.data.repository.backend.retrofit.messages

import com.saulhervas.easychat.data.repository.response.new_message.NewMessageRequest
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.messages_list.MessagesModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessagesDataProvider @Inject constructor(
    private val remoteDataSource: MessagesDataSource
) {
    fun getMessagesList(id: String, offset: Int, limit: Int): Flow<BaseResponse<MessagesModel>> {
        return remoteDataSource.getMessagesLists(id, offset, limit)
    }

    fun newMessage(newMessageRequest: NewMessageRequest): Flow<BaseResponse<NewMessageResponse>> {
        return remoteDataSource.newMessage(newMessageRequest)
    }
}