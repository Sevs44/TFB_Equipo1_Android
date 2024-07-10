package com.saulhervas.easychat.data.repository.response.register

import com.google.gson.annotations.SerializedName
import com.saulhervas.easychat.domain.model.modelsregister.UserRegister


data class RegisterResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("user")
    val user: UserRegister
)
