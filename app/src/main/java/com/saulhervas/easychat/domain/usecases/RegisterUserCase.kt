package com.saulhervas.easychat.domain.usecases

import com.saulhervas.easychat.data.repository.backend.retrofit.users.RegisterDataProvider
import com.saulhervas.easychat.data.repository.response.register.RegisterRequest
import com.saulhervas.easychat.data.repository.response.register.RegisterResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUserCase @Inject constructor(
    val registerProvider: RegisterDataProvider
) {
    operator fun invoke(registerRequest: RegisterRequest): Flow<BaseResponse<RegisterResponse>> {
        return registerProvider.getRegisterData(registerRequest)
    }
}