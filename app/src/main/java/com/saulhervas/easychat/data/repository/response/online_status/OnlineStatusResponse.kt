package com.saulhervas.easychat.data.repository.response.online_status

import com.google.gson.annotations.SerializedName

data class OnlineStatusResponse(
    @SerializedName("message")
    val message: String
)