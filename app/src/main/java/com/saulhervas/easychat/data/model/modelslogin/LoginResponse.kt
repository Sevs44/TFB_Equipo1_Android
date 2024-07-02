package com.saulhervas.easychat.data.model.modelslogin

import com.saulhervas.easychat.data.model.User

data class LoginResponse(
    val token: String,
    val user: User
)
