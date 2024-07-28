package com.saulhervas.easychat.domain.mappers

import com.saulhervas.easychat.data.repository.response.user_list.UserListResponse
import com.saulhervas.easychat.domain.model.UserNewChatItemModel

class UserListMapper {
    companion object {
        fun userListResponseToUserListModel(userListResponse: UserListResponse?): ArrayList<UserNewChatItemModel> {
            val list: ArrayList<UserNewChatItemModel> = arrayListOf()
            userListResponse?.forEach { user ->
                list.add(
                    UserNewChatItemModel(
                        id = user.id,
                        nick = user.nick,
                        onlineStatus = user.onlineStatus
                    )
                )
            }
            return list
        }
    }
}