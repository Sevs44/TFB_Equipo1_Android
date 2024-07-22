package com.saulhervas.easychat.data.repository.response.new_message

import com.google.gson.annotations.SerializedName

data class NewMessageResponse(
    @SerializedName("success")
    val success: String,
)
