package com.saulhervas.easychat.data.repository.backend.retrofit.users

import com.saulhervas.easychat.data.repository.response.login.LoginRequest
import com.saulhervas.easychat.data.repository.response.login.LoginResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginDataProvider @Inject constructor(
    val remoteDataSource: LoginDataSource
) {
    fun getLogin(loginRequest: LoginRequest): Flow<BaseResponse<LoginResponse>> {
        return remoteDataSource.getLogin(loginRequest)
    }
}