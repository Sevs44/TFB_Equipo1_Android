package com.saulhervas.easychat.data.repository.backend.retrofit.users

import com.saulhervas.easychat.data.repository.backend.retrofit.BaseService
import com.saulhervas.easychat.data.repository.response.logout.LogoutResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LogoutDataSource @Inject constructor(
    private val chatsCalls: LogoutCalls
) : BaseService() {
    fun getLogoutUser(token: String): Flow<BaseResponse<LogoutResponse>> =
        flow {
            val apiResult = chatsCalls.callLogout(token)
            if (apiResult is BaseResponse.Success) {
                emit(BaseResponse.Success(apiResult.data))
            } else if (apiResult is BaseResponse.Error) {
                emit(BaseResponse.Error(apiResult.error))
            }
        }
}