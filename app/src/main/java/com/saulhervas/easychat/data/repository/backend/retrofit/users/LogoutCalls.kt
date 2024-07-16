package com.saulhervas.easychat.data.repository.backend.retrofit.users

import com.saulhervas.easychat.data.repository.backend.retrofit.ApiService
import com.saulhervas.easychat.data.repository.backend.retrofit.BaseService
import com.saulhervas.easychat.data.repository.response.logout.LogoutResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import javax.inject.Inject

class LogoutCalls @Inject constructor(
    private val apiService: ApiService
) : BaseService() {
    suspend fun callLogout(token: String): BaseResponse<LogoutResponse> {
        return apiCall { apiService.postLogoutUser(token) }
    }
}