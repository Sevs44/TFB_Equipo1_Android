package com.saulhervas.easychat.data.repository.response.new_message

import com.google.gson.annotations.SerializedName

data class NewMessageRequest(
    @SerializedName("chat")
    val idChat: String,
    @SerializedName("source")
    val sentBy: String,
    @SerializedName("message")
    val messageContent: String
)
