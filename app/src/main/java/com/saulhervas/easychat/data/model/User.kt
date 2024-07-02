package com.saulhervas.easychat.data.model

data class User(
    val id: String,
    val nick: String,
    val avatar: String?,
    val online: Boolean
)
