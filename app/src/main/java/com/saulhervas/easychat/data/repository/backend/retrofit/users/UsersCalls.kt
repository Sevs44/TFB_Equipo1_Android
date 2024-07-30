package com.saulhervas.easychat.data.repository.backend.retrofit.users

import com.saulhervas.easychat.data.repository.backend.retrofit.ApiService
import com.saulhervas.easychat.data.repository.backend.retrofit.BaseService
import com.saulhervas.easychat.data.repository.response.login.LoginRequest
import com.saulhervas.easychat.data.repository.response.login.LoginResponse
import com.saulhervas.easychat.data.repository.response.logout.LogoutResponse
import com.saulhervas.easychat.data.repository.response.online_status.OnlineStatusResponse
import com.saulhervas.easychat.data.repository.response.profile.UserProfileResponse
import com.saulhervas.easychat.data.repository.response.register.RegisterRequest
import com.saulhervas.easychat.data.repository.response.register.RegisterResponse
import com.saulhervas.easychat.data.repository.response.user_list.UserListResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import javax.inject.Inject

class UsersCalls @Inject constructor(
    private val apiService: ApiService,
) : BaseService() {
    suspend fun callLogin(loginRequest: LoginRequest): BaseResponse<LoginResponse> {
        return apiCall { apiService.loginUser(loginRequest) }
    }
    suspend fun callRegisterUser(registerRequest: RegisterRequest): BaseResponse<RegisterResponse> {
        return apiCall { apiService.registerUser(registerRequest) }
    }

    suspend fun callUserProfile(): BaseResponse<UserProfileResponse> {
        return apiCall { apiService.getProfile() }
    }

    suspend fun callLogout(): BaseResponse<LogoutResponse> {
        return apiCall { apiService.postLogoutUser() }
    }

    suspend fun callBiometric(): BaseResponse<LoginResponse> {
        return apiCall { apiService.postBiometric() }
    }

    suspend fun callUserList(): BaseResponse<UserListResponse> {
        return apiCall { apiService.getUserList() }
    }

    suspend fun callUserOnlineTrue(): BaseResponse<OnlineStatusResponse> {
        return apiCall { apiService.putOnlineStatusTrue() }
    }

    suspend fun callUserOnlineFalse(): BaseResponse<OnlineStatusResponse> {
        return apiCall { apiService.putOnlineStatusFalse() }
    }
}