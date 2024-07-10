package com.saulhervas.easychat.domain.usecases

import com.saulhervas.easychat.data.repository.backend.retrofit.users.LoginDataProvider
import com.saulhervas.easychat.data.repository.response.login.LoginRequest
import com.saulhervas.easychat.data.repository.response.login.LoginResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    val dataProvider: LoginDataProvider
) {
    operator fun invoke(loginRequest: LoginRequest): Flow<BaseResponse<LoginResponse>> {
        return dataProvider.getLogin(loginRequest)
    }
}