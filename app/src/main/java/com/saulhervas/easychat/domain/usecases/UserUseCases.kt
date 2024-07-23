package com.saulhervas.easychat.domain.usecases

import com.saulhervas.easychat.data.repository.backend.retrofit.users.UsersDataProvider
import com.saulhervas.easychat.data.repository.response.login.LoginRequest
import com.saulhervas.easychat.data.repository.response.login.LoginResponse
import com.saulhervas.easychat.data.repository.response.logout.LogoutResponse
import com.saulhervas.easychat.data.repository.response.register.RegisterRequest
import com.saulhervas.easychat.data.repository.response.register.RegisterResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserUseCases @Inject constructor(
    private val userDataProvider: UsersDataProvider
) {
    fun registerUser(registerRequest: RegisterRequest): Flow<BaseResponse<RegisterResponse>> {
        return userDataProvider.getRegisterData(registerRequest)
    }
    fun loginUser(loginRequest: LoginRequest): Flow<BaseResponse<LoginResponse>> {
        return userDataProvider.getLogin(loginRequest)
    }
    fun logoutUser(): Flow<BaseResponse<LogoutResponse>> {
        return userDataProvider.getLogoutUser()
    }

    fun biometricUser(): Flow<BaseResponse<LoginResponse>> {
        return userDataProvider.getBiometricUser()
    }
}