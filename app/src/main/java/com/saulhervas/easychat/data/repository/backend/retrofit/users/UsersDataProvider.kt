package com.saulhervas.easychat.data.repository.backend.retrofit.users

import com.saulhervas.easychat.data.repository.response.login.LoginRequest
import com.saulhervas.easychat.data.repository.response.login.LoginResponse
import com.saulhervas.easychat.data.repository.response.logout.LogoutResponse
import com.saulhervas.easychat.data.repository.response.online_status.OnlineStatusResponse
import com.saulhervas.easychat.data.repository.response.profile.UserProfileResponse
import com.saulhervas.easychat.data.repository.response.register.RegisterRequest
import com.saulhervas.easychat.data.repository.response.register.RegisterResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.UserNewChatItemModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsersDataProvider @Inject constructor(
    private val remoteDataSource: UsersDataSource
) {
    fun getRegisterData(registerRequest: RegisterRequest): Flow<BaseResponse<RegisterResponse>> {
        return remoteDataSource.getRegisterUser(registerRequest)
    }

    fun getLogin(loginRequest: LoginRequest): Flow<BaseResponse<LoginResponse>> {
        return remoteDataSource.getLogin(loginRequest)
    }
    fun getUserName(): Flow<BaseResponse<UserProfileResponse>> {
        return remoteDataSource.getUserProfile()
    }

    fun getLogoutUser(): Flow<BaseResponse<LogoutResponse>> {
        return remoteDataSource.getLogoutUser()
    }

    fun getBiometricUser(): Flow<BaseResponse<LoginResponse>> {
        return remoteDataSource.getBiometricUser()
    }

    fun getUserList(): Flow<BaseResponse<ArrayList<UserNewChatItemModel>>> {
        return remoteDataSource.getUserList()
    }

    fun getOnlineTrue(): Flow<BaseResponse<OnlineStatusResponse>> {
        return remoteDataSource.getOnlineTrue()
    }

    fun getOnlineFalse(): Flow<BaseResponse<OnlineStatusResponse>> {
        return remoteDataSource.getOnlineFalse()
    }
}