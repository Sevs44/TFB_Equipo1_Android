package com.saulhervas.easychat.domain.model.modelslogin

import com.google.gson.annotations.SerializedName

data class UserLogin(
    @SerializedName("id")
    val id: String,
    @SerializedName("nick")
    val nick: String,
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("online")
    val online: Boolean
)
