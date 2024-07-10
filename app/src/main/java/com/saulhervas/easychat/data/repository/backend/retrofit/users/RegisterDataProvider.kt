package com.saulhervas.easychat.data.repository.backend.retrofit.users

import com.saulhervas.easychat.data.repository.response.register.RegisterRequest
import com.saulhervas.easychat.data.repository.response.register.RegisterResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterDataProvider @Inject constructor(
    val registerDataSource: RegisterDataSource
) {
    fun getRegisterData(registerRequest: RegisterRequest): Flow<BaseResponse<RegisterResponse>> {
        return registerDataSource.getRegisterUser(registerRequest)
    }
}