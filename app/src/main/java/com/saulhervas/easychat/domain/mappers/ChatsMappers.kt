package com.saulhervas.easychat.domain.mappers

import com.saulhervas.easychat.data.repository.response.chats.OpenChatsResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel

class ChatsMappers {
    companion object {
        fun openChatsResponseToOpenChatsModel(idUser: String, openChatsResponse: OpenChatsResponse?): ArrayList<OpenChatItemModel> {
            val list: ArrayList<OpenChatItemModel> = arrayListOf()
            openChatsResponse?.map {
                if (it.idUser1 == idUser) {
                    list.add(
                        OpenChatItemModel(
                            idChat = it.id,
                            idTargetUser = it.idUser2,
                            nickTargetUser = it.user2Nick,
                            isOnlineUser = it.user2IsOnline,
                        )
                    )
                } else {
                    list.add(
                        OpenChatItemModel(
                            idChat = it.id,
                            idTargetUser = it.idUser1,
                            nickTargetUser = it.user1Nick,
                            isOnlineUser = it.user1IsOnline,
                        )
                    )
                }
            }
            return list
        }

    }
}