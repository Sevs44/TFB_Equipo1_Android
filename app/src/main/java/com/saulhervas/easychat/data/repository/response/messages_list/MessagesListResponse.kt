package com.saulhervas.easychat.data.repository.response.messages_list

import com.google.gson.annotations.SerializedName

data class MessagesListResponse(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("rows")
    val messages: ArrayList<MessageItemResponse>?
)

data class MessageItemResponse(
    @SerializedName("chat")
    val idChat: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("id")
    val idMessage: String?,
    @SerializedName("message")
    val messageContent: String?,
    @SerializedName("source")
    val sentBy: String?
)