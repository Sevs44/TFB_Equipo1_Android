package com.saulhervas.easychat.domain.model.messages_list

data class MessagesModel(
val nMessages: Int?,
val messageList: ArrayList<MessageItemModel>?
)

data class MessageItemModel(
    val messageSentAt: String?,
    val messageContent: String?,
    val sentBy: String?
)