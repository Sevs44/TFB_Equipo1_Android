package com.saulhervas.easychat.data.repository.backend.retrofit.users

import com.saulhervas.easychat.data.repository.backend.retrofit.ApiService
import com.saulhervas.easychat.data.repository.backend.retrofit.BaseService
import com.saulhervas.easychat.data.repository.response.login.LoginRequest
import com.saulhervas.easychat.data.repository.response.login.LoginResponse
import com.saulhervas.easychat.data.repository.response.register.RegisterRequest
import com.saulhervas.easychat.data.repository.response.register.RegisterResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import javax.inject.Inject

class UserCalls @Inject constructor(
    val apiService: ApiService
) : BaseService() {
    suspend fun callLogin(loginRequest: LoginRequest): BaseResponse<LoginResponse> {
        return apiCall { apiService.loginUser(loginRequest) }
    }
    suspend fun callRegisterUser(registerRequest: RegisterRequest): BaseResponse<RegisterResponse> {
        return apiCall { apiService.registerUser(registerRequest) }
    }
}