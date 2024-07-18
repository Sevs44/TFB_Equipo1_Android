package com.saulhervas.easychat.data.repository.response.user_list

import com.google.gson.annotations.SerializedName

class UserListResponse : ArrayList<UserItem>()

data class UserItem(
    @SerializedName("id")
    val id: String?,
    @SerializedName("nick")
    val nick: String?,
    @SerializedName("online")
    val onlineStatus: Boolean?,
)