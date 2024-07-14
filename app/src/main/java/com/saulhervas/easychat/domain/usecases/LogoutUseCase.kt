package com.saulhervas.easychat.domain.usecases

import com.saulhervas.easychat.data.repository.backend.retrofit.users.LogoutDataProvider
import com.saulhervas.easychat.data.repository.response.logout.LogoutResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    val dataProvider: LogoutDataProvider
) {
    operator fun invoke(token: String): Flow<BaseResponse<LogoutResponse>> {
        return dataProvider.getLogoutUser(token)
    }
}