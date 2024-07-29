package com.saulhervas.easychat.data.repository.response.register

import com.google.gson.annotations.SerializedName


data class RegisterResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("user")
    val user: UserRegister
)
