package com.saulhervas.easychat.data.repository.response.register

import com.google.gson.annotations.SerializedName

data class UserRegister(
    @SerializedName("id")
    val id: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("token")
    val token: String
)
