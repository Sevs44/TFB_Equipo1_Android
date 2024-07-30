package com.saulhervas.easychat.data.repository.response.new_chat

import com.google.gson.annotations.SerializedName

data class NewChatRequest(
    @SerializedName("source")
    val idSource: String,
    @SerializedName("target")
    val idTarget: String
)