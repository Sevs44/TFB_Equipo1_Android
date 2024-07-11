package com.saulhervas.easychat.domain.usecases

import com.saulhervas.easychat.data.repository.backend.retrofit.chats.OpenChatsDataProvider
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OpenChatUseCases @Inject constructor(
    private val dataProvider: OpenChatsDataProvider
) {
    operator fun invoke(token: String): Flow<BaseResponse<ArrayList<OpenChatItemModel>>> {
        return dataProvider.getOpenChatList(token)
    }
}