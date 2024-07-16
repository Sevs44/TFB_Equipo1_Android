package com.saulhervas.easychat.data.repository.backend.retrofit.chats

import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatsDataProvider @Inject constructor(
    private val remoteDataSource: ChatsDataSource
) {
    fun getOpenChatList(token: String): Flow<BaseResponse<ArrayList<OpenChatItemModel>>> {
        return remoteDataSource.getOpenChats(token)
    }
}