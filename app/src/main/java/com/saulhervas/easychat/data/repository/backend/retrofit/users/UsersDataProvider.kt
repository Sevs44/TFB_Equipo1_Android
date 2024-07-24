package com.saulhervas.easychat.data.repository.backend.retrofit.users

import com.saulhervas.easychat.data.repository.response.login.LoginRequest
import com.saulhervas.easychat.data.repository.response.login.LoginResponse
import com.saulhervas.easychat.data.repository.response.logout.LogoutResponse
import com.saulhervas.easychat.data.repository.response.profile.UserProfileResponse
import com.saulhervas.easychat.data.repository.response.register.RegisterRequest
import com.saulhervas.easychat.data.repository.response.register.RegisterResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.UserNewChatItemModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsersDataProvider @Inject constructor(
    val remoteDataSource: UsersDataSource
) {
    fun getRegisterData(registerRequest: RegisterRequest): Flow<BaseResponse<RegisterResponse>> {
        return remoteDataSource.getRegisterUser(registerRequest)
    }

    fun getLogin(loginRequest: LoginRequest): Flow<BaseResponse<LoginResponse>> {
        return remoteDataSource.getLogin(loginRequest)
    }

    fun getUserName(token: String): Flow<BaseResponse<UserProfileResponse>> {
        return remoteDataSource.getUserProfile(token)
    }

    fun getLogoutUser(token: String): Flow<BaseResponse<LogoutResponse>> {
        return remoteDataSource.getLogoutUser(token)
    }

    fun getBiometricUser(token: String): Flow<BaseResponse<LoginResponse>> {
        return remoteDataSource.getBiometricUser(token)
    }

    fun getUserList(token: String): Flow<BaseResponse<ArrayList<UserNewChatItemModel>>> {
        return remoteDataSource.getUserList(token)
    }
}