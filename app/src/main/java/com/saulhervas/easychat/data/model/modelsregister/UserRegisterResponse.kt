package com.saulhervas.easychat.data.model.modelsregister

import com.google.gson.annotations.SerializedName

data class UserRegisterResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("token")
    val token: String
)
