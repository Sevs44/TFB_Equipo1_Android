package com.saulhervas.easychat.data.repository.backend.retrofit.users

import com.saulhervas.easychat.data.repository.response.logout.LogoutResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogoutDataProvider @Inject constructor(
    val remoteDataSource: LogoutDataSource
) {
    fun getLogoutUser(token: String): Flow<BaseResponse<LogoutResponse>> {
        return remoteDataSource.getLogoutUser(token)
    }

    fun getToken(token: String): String {
        return token
    }
}