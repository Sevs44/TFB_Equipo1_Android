package com.saulhervas.easychat.data.repository.backend.retrofit.users

import com.saulhervas.easychat.data.repository.backend.retrofit.ApiService
import com.saulhervas.easychat.data.repository.backend.retrofit.BaseService
import com.saulhervas.easychat.data.repository.response.login.LoginRequest
import com.saulhervas.easychat.data.repository.response.login.LoginResponse
import com.saulhervas.easychat.data.repository.response.logout.LogoutResponse
import com.saulhervas.easychat.data.repository.response.profile.UserProfileResponse
import com.saulhervas.easychat.data.repository.response.register.RegisterRequest
import com.saulhervas.easychat.data.repository.response.register.RegisterResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import javax.inject.Inject

class UsersCalls @Inject constructor(
    private val apiService: ApiService
) : BaseService() {
    suspend fun callLogin(loginRequest: LoginRequest): BaseResponse<LoginResponse> {
        return apiCall { apiService.loginUser(loginRequest) }
    }
    suspend fun callRegisterUser(registerRequest: RegisterRequest): BaseResponse<RegisterResponse> {
        return apiCall { apiService.registerUser(registerRequest) }
    }

    suspend fun callUserProfile(token: String): BaseResponse<UserProfileResponse> {
        return apiCall { apiService.getProfile(token) }
    }
    suspend fun callLogout(token: String): BaseResponse<LogoutResponse> {
        return apiCall { apiService.postLogoutUser(token) }
    }

    suspend fun callBiometric(token: String): BaseResponse<LoginResponse> {
        return apiCall { apiService.postBiometric(token) }
    }
}