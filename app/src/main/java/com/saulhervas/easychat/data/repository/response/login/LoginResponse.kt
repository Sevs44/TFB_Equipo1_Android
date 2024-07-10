package com.saulhervas.easychat.data.repository.response.login

import com.google.gson.annotations.SerializedName
import com.saulhervas.easychat.domain.model.modelslogin.UserLogin

data class LoginResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val userLogin: UserLogin
)
