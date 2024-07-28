package com.saulhervas.easychat.data.repository.response.chats

import com.google.gson.annotations.SerializedName

class OpenChatsResponse : ArrayList<OpenChatsItem>()

data class OpenChatsItem(
    @SerializedName("chat")
    val id: String?,
    @SerializedName("chatcreated")
    val chatCreatedAt: String?,
    @SerializedName("source")
    val idSource: String?,
    @SerializedName("sourceavatar")
    val sourceAvatar: String?,
    @SerializedName("sourcenick")
    val sourceNick: String?,
    @SerializedName("sourceonline")
    val sourceIsOnline: Boolean?,
    @SerializedName("sourcetoken")
    val sorceToken: String?,
    @SerializedName("target")
    val idTarget: String?,
    @SerializedName("targetavatar")
    val targetAvatar: String?,
    @SerializedName("targetnick")
    val targetNick: String?,
    @SerializedName("targetonline")
    val targetIsOnline: Boolean?,
    @SerializedName("targettoken")
    val targetToken: String?
)