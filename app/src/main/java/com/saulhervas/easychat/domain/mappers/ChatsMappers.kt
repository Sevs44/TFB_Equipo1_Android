package com.saulhervas.easychat.domain.mappers

import com.saulhervas.easychat.data.repository.response.chats.OpenChatsResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel

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
                        )
                    )
                } else {
                    list.add(
                        OpenChatItemModel(
                            idChat = it.id,
                            idTargetUser = it.idSource,
                            nickTargetUser = it.sourceNick,
                            isOnlineUser = it.sourceIsOnline,
                        )
                    )
                }
            }
            return list
        }

    }
}