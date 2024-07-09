package com.saulhervas.easychat.data.repository.response.chats

import com.google.gson.annotations.SerializedName

class OpenChatsResponse : ArrayList<OpenChatsItem>()

data class OpenChatsItem(
    @SerializedName("chat")
    val id: String?,
    @SerializedName("chatcreated")
    val chatCreatedAt: String?,
    @SerializedName("source")
    val idUser1: String?,
    @SerializedName("sourceavatar")
    val user1Avatar: String?,
    @SerializedName("sourcenick")
    val user1Nick: String?,
    @SerializedName("sourceonline")
    val user1IsOnline: Boolean?,
    @SerializedName("sourcetoken")
    val user1Token: String?,
    @SerializedName("target")
    val idUser2: String?,
    @SerializedName("targetavatar")
    val user2Avatar: String?,
    @SerializedName("targetnick")
    val user2Nick: String?,
    @SerializedName("targetonline")
    val user2IsOnline: Boolean?,
    @SerializedName("targettoken")
    val user2Token: String?
)