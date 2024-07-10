package com.saulhervas.easychat.data.repository.backend.retrofit.users

import com.saulhervas.easychat.data.repository.backend.retrofit.BaseService
import com.saulhervas.easychat.data.repository.response.register.RegisterRequest
import com.saulhervas.easychat.data.repository.response.register.RegisterResponse
import com.saulhervas.easychat.domain.mappers.Mapper
import com.saulhervas.easychat.domain.model.BaseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject


class RegisterDataSource @Inject constructor(
    val registerCalls: RegisterCalls
) : BaseService() {
    fun getRegisterUser(registerRequest: RegisterRequest): Flow<BaseResponse<RegisterResponse>> =
        flow {
            val apiResult = registerCalls.callRegisterUser(registerRequest)
            if (apiResult is BaseResponse.Success) {
                emit(BaseResponse.Success(Mapper.UserRegisterToUserModel(apiResult.data)))
            } else if (apiResult is BaseResponse.Error) {
                emit(BaseResponse.Error(apiResult.error))
            }
        }
}