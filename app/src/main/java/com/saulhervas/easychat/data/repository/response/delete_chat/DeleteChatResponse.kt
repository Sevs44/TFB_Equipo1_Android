package com.saulhervas.easychat.data.repository.response.delete_chat

import com.google.gson.annotations.SerializedName

data class DeleteChatResponse(
    @SerializedName("success")
    val success: Boolean
)
