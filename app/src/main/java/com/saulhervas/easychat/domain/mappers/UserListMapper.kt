package com.saulhervas.easychat.domain.mappers

import com.saulhervas.easychat.data.repository.response.user_list.UserListResponse
import com.saulhervas.easychat.domain.model.UserItemModel

class UserListMapper {
    companion object {
        fun userListResponseToUserListModel(userListResponse: UserListResponse?): ArrayList<UserItemModel> {
            val list: ArrayList<UserItemModel> = arrayListOf()
            userListResponse?.forEach { user ->
                list.add(
                    UserItemModel(
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