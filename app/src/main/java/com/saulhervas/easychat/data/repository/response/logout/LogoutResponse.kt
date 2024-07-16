package com.saulhervas.easychat.data.repository.response.logout

import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("message")
    val message: String
)