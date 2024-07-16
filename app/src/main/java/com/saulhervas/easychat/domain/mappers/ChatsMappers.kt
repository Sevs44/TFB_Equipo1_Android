package com.saulhervas.easychat.domain.mappers

import com.saulhervas.easychat.data.repository.response.chats.OpenChatsResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel

class ChatsMappers {
    companion object {
        fun openChatsResponseToOpenChatsModel(openChatsResponse: OpenChatsResponse?): ArrayList<OpenChatItemModel> {
            val list: ArrayList<OpenChatItemModel> = arrayListOf()
            openChatsResponse?.forEach {
                list.add(
                    OpenChatItemModel(
                        id = it.id,
                        idUser1 = it.idUser1,
                        nickUser1 = it.user1Nick,
                        isOnlineUser1 = it.user1IsOnline,
                        idUser2 = it.idUser2,
                        nickUser2 = it.user2Nick,
                        isOnlineUser2 = it.user2IsOnline,
                        )
                )
            }
            return list
        }

    }
}