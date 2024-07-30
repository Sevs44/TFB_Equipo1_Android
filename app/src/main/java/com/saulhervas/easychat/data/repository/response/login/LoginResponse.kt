package com.saulhervas.easychat.data.repository.response.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val userLogin: UserLogin
)
