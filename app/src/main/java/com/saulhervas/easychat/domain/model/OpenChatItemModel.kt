package com.saulhervas.easychat.domain.model

data class OpenChatItemModel(
    val idChat: String?,
    val idTargetUser: String?,
    val nickTargetUser: String?,
    val isOnlineUser: Boolean?,
    val idSource: String?,
    val chatCreatedAt: String?,
)