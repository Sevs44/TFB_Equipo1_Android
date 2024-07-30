package com.saulhervas.easychat.domain.mappers

import com.saulhervas.easychat.data.repository.response.register.RegisterResponse
import com.saulhervas.easychat.data.repository.response.register.UserRegister

class UsersMappers {
    companion object {
        fun userRegisterToUserModel(userRegister: RegisterResponse): RegisterResponse {
            val list = RegisterResponse(
                success = userRegister.success,
                user = UserRegister(
                    id = userRegister.user.id,
                    login = userRegister.user.login,
                    password = userRegister.user.password,
                    token = userRegister.user.token
                )
            )
            return list
        }
    }
}