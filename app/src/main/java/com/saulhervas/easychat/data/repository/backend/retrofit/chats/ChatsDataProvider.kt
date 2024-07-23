package com.saulhervas.easychat.data.repository.backend.retrofit.chats

import com.saulhervas.easychat.data.repository.response.new_chat.NewChatRequest
import com.saulhervas.easychat.data.repository.response.new_chat.NewChatResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatsDataProvider @Inject constructor(
    private val remoteDataSource: ChatsDataSource
) {
    fun getOpenChatList(): Flow<BaseResponse<ArrayList<OpenChatItemModel>>> {
        return remoteDataSource.getOpenChats()
    }

    fun newChat(
        token: String,
        newChatRequest: NewChatRequest
    ): Flow<BaseResponse<NewChatResponse>> {
        return remoteDataSource.newChat(token, newChatRequest)
    }
}