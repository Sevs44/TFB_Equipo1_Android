package com.saulhervas.easychat.data.repository.backend.retrofit.messages

import android.util.Log
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import com.saulhervas.easychat.domain.model.messages_list.MessagesModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessagesDataProvider @Inject constructor(
    private val remoteDataSource: MessagesDataSource
) {
    fun getMessagesList(token: String, id: String, offset: Int, limit: Int): Flow<BaseResponse<MessagesModel>> {
        return remoteDataSource.getMessagesLists(token, id, offset, limit)
    }
}