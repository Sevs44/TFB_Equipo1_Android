package com.saulhervas.easychat.domain.usecases

import com.saulhervas.easychat.data.repository.backend.retrofit.chats.ChatsDataProvider
import com.saulhervas.easychat.data.repository.response.new_chat.NewChatRequest
import com.saulhervas.easychat.data.repository.response.new_chat.NewChatResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatUseCases @Inject constructor(
    private val dataProvider: ChatsDataProvider
) {
    fun getOpenChats(token: String): Flow<BaseResponse<ArrayList<OpenChatItemModel>>> {
        return dataProvider.getOpenChatList(token)
    }

    fun newChat(
        token: String,
        newChatRequest: NewChatRequest
    ): Flow<BaseResponse<NewChatResponse>> {
        return dataProvider.newChat(token, newChatRequest)
    }
}