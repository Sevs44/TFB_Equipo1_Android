package com.saulhervas.easychat.data.repository.response.profile


import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("nick")
    val nick: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("platform")
    val platform: String,
    @SerializedName("uuid")
    val uuid: Any,
    @SerializedName("token")
    val token: String,
    @SerializedName("online")
    val online: Boolean,
    @SerializedName("created")
    val created: String,
    @SerializedName("updated")
    val updated: String
)