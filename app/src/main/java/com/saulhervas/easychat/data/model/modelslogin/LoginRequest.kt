package com.saulhervas.easychat.data.model.modelslogin

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("password")
    val password: String,
    @SerializedName("login")
    val login: String
)