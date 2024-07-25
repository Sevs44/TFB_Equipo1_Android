package com.saulhervas.easychat.data.repository.backend.retrofit.chats

import android.content.Context
import com.saulhervas.easychat.data.repository.backend.retrofit.BaseService
import com.saulhervas.easychat.data.repository.response.new_chat.NewChatRequest
import com.saulhervas.easychat.data.repository.response.new_chat.NewChatResponse
import com.saulhervas.easychat.domain.mappers.ChatsMappers
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import com.saulhervas.easychat.domain.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatsDataSource @Inject constructor(
    private val userSession: UserSession,
    private val chatsCalls: ChatsCalls,
    //val context: Context
) : BaseService() {
    fun getOpenChats(): Flow<BaseResponse<ArrayList<OpenChatItemModel>>> =
        flow {
            val apiResult = chatsCalls.callOpenChats()
            if (apiResult is BaseResponse.Success) {
                emit(BaseResponse.Success(ChatsMappers.openChatsResponseToOpenChatsModel(userSession.id, apiResult.data)))
            } else if (apiResult is BaseResponse.Error) {
                emit(BaseResponse.Error(apiResult.error))
            }
        }

    fun newChat(
        newChatRequest: NewChatRequest
    ): Flow<BaseResponse<NewChatResponse>> =
        flow {
            val apiResult = chatsCalls.callNewChat(newChatRequest)
            if (apiResult is BaseResponse.Success) {
                emit(BaseResponse.Success(apiResult.data))
            } else if (apiResult is BaseResponse.Error) {
                emit(BaseResponse.Error(apiResult.error))
            }
        }
}