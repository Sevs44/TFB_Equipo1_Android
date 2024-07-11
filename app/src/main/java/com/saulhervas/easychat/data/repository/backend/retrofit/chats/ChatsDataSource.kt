package com.saulhervas.easychat.data.repository.backend.retrofit.chats

import com.saulhervas.easychat.data.repository.backend.retrofit.BaseService
import com.saulhervas.easychat.domain.mappers.Mapper.Companion.OpenChatsResponseToOpenChatsModel
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatsDataSource @Inject constructor(
    private val chatsCalls: ChatsCalls
) : BaseService() {
    fun getOpenChats(token: String): Flow<BaseResponse<ArrayList<OpenChatItemModel>>> =
        flow {
            val apiResult = chatsCalls.callOpenChats(token)
            if (apiResult is BaseResponse.Success) {
                emit(BaseResponse.Success(OpenChatsResponseToOpenChatsModel(apiResult.data)))
            } else if (apiResult is BaseResponse.Error) {
                emit(BaseResponse.Error(apiResult.error))
            }
        }
}