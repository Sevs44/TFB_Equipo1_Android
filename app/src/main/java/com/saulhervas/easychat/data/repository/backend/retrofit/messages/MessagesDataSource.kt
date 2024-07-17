package com.saulhervas.easychat.data.repository.backend.retrofit.messages

import android.util.Log
import com.saulhervas.easychat.data.repository.backend.retrofit.BaseService
import com.saulhervas.easychat.domain.mappers.ChatsMappers
import com.saulhervas.easychat.domain.mappers.MessagesMappers
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import com.saulhervas.easychat.domain.model.messages_list.MessagesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MessagesDataSource @Inject constructor(
    private val messagesCalls: MessagesCalls
) : BaseService() {
    fun getMessagesLists(token: String, id: String, offset: Int, limit: Int): Flow<BaseResponse<MessagesModel>> =
        flow {
            val apiResult = messagesCalls.callMessageList(token, id, offset, limit)
            if (apiResult is BaseResponse.Success) {
                emit(BaseResponse.Success(MessagesMappers.messagesListResponseToMessagesListModel(apiResult.data)))
            } else if (apiResult is BaseResponse.Error) {
                emit(BaseResponse.Error(apiResult.error))
            }
        }
}