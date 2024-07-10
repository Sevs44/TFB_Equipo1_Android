package com.saulhervas.easychat.data.repository.response.register

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String,
)