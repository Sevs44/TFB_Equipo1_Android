package com.saulhervas.easychat.domain.mappers

import com.saulhervas.easychat.data.repository.response.chats.OpenChatsResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import com.saulhervas.easychat.utils.DateFormatter

class ChatsMappers {
    companion object {
        fun openChatsResponseToOpenChatsModel(idUser: String, openChatsResponse: OpenChatsResponse?): ArrayList<OpenChatItemModel> {
            val list: ArrayList<OpenChatItemModel> = arrayListOf()
            openChatsResponse?.map {
                if (it.idSource == idUser) {
                    list.add(
                        OpenChatItemModel(
                            idChat = it.id,
                            idTargetUser = it.idTarget,
                            nickTargetUser = it.targetNick,
                            isOnlineUser = it.targetIsOnline,
                            idSource = it.idSource,
                            chatCreatedAt = DateFormatter.formatDay(it.chatCreatedAt)
                        )
                    )
                } else {
                    list.add(
                        OpenChatItemModel(
                            idChat = it.id,
                            idTargetUser = it.idSource,
                            nickTargetUser = it.sourceNick,
                            isOnlineUser = it.sourceIsOnline,
                            idSource = it.idSource,
                            chatCreatedAt = DateFormatter.formatDay(it.chatCreatedAt)
                        )
                    )
                }
            }
            return list
        }

    }
}