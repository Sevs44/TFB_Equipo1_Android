package com.saulhervas.easychat.domain.mappers

import com.saulhervas.easychat.data.repository.response.messages_list.MessagesListResponse
import com.saulhervas.easychat.domain.model.messages_list.MessageItemModel
import com.saulhervas.easychat.domain.model.messages_list.MessagesModel
import com.saulhervas.easychat.utils.DateFormatter

class MessagesMappers {
    companion object {
        fun messagesListResponseToMessagesListModel(messagesResponse: MessagesListResponse): MessagesModel {
            val list: ArrayList<MessageItemModel> = arrayListOf()
            messagesResponse.messages?.forEach {
                list.add(
                    MessageItemModel(
                        idMessage = it.idMessage,
                        messageSentAt = DateFormatter.formatHour(it.date),
                        messageContent = it.messageContent,
                        sentBy = it.sentBy,
                    )
                )
            }
            return MessagesModel(
                nMessages = messagesResponse.count,
                messageList = list
            )
        }
    }
}