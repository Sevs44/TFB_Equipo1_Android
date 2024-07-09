package com.saulhervas.easychat.data.repository.backend.retrofit

import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OpenChatsDataProvider @Inject constructor(
    val remoteDataSource: ChatsDataSource
) {
    fun getOpenChatList(): Flow<BaseResponse<ArrayList<OpenChatItemModel>>> {
        return remoteDataSource.getOpenChats()
    }
}