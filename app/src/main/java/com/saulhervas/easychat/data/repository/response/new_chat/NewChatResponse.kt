package com.saulhervas.easychat.data.repository.response.new_chat

import com.google.gson.annotations.SerializedName

data class NewChatResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("created")
    val created: Boolean
)