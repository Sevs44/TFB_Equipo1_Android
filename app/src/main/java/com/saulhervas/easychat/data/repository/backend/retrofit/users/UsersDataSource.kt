package com.saulhervas.easychat.data.repository.backend.retrofit.users

import android.content.Context
import com.saulhervas.easychat.data.repository.backend.retrofit.BaseService
import com.saulhervas.easychat.data.repository.response.login.LoginRequest
import com.saulhervas.easychat.data.repository.response.login.LoginResponse
import com.saulhervas.easychat.data.repository.response.logout.LogoutResponse
import com.saulhervas.easychat.data.repository.response.profile.UserProfileResponse
import com.saulhervas.easychat.data.repository.response.register.RegisterRequest
import com.saulhervas.easychat.data.repository.response.register.RegisterResponse
import com.saulhervas.easychat.domain.mappers.UserListMapper
import com.saulhervas.easychat.domain.mappers.UsersMappers
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.UserItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UsersDataSource @Inject constructor(
    private val userCalls: UsersCalls,
    //val context: Context
) : BaseService() {
    fun getLogin(loginRequest: LoginRequest): Flow<BaseResponse<LoginResponse>> =
        flow {
            val apiResult = userCalls.callLogin(loginRequest)
            if (apiResult is BaseResponse.Success) {
                emit(BaseResponse.Success(apiResult.data))
            } else if (apiResult is BaseResponse.Error) {
                emit(BaseResponse.Error(apiResult.error))
            }
        }

    fun getRegisterUser(registerRequest: RegisterRequest): Flow<BaseResponse<RegisterResponse>> =
        flow {
            val apiResult = userCalls.callRegisterUser(registerRequest)
            if (apiResult is BaseResponse.Success) {
                emit(BaseResponse.Success(UsersMappers.userRegisterToUserModel(apiResult.data)))
            } else if (apiResult is BaseResponse.Error) {
                emit(BaseResponse.Error(apiResult.error))
            }
        }

    fun getLogoutUser(): Flow<BaseResponse<LogoutResponse>> =
        flow {
            val apiResult = userCalls.callLogout()
            if (apiResult is BaseResponse.Success) {
                emit(BaseResponse.Success(apiResult.data))
            } else if (apiResult is BaseResponse.Error) {
                emit(BaseResponse.Error(apiResult.error))
            }
        }

    fun getBiometricUser(): Flow<BaseResponse<LoginResponse>> =
        flow {
            val apiResult = userCalls.callBiometric()
            if (apiResult is BaseResponse.Success) {
                emit(BaseResponse.Success(apiResult.data))
            } else if (apiResult is BaseResponse.Error) {
                emit(BaseResponse.Error(apiResult.error))
            }
        }

    fun getUserList(): Flow<BaseResponse<ArrayList<UserItemModel>>> =
        flow {
            val apiResult = userCalls.callUserList()
            if (apiResult is BaseResponse.Success) {
                emit(BaseResponse.Success(UserListMapper.userListResponseToUserListModel(apiResult.data)))
            } else if (apiResult is BaseResponse.Error) {
                emit(BaseResponse.Error(apiResult.error))
            }
        }

    fun getUserProfile(): Flow<BaseResponse<UserProfileResponse>> =
        flow {
            val apiResult = userCalls.callUserProfile()
            if (apiResult is BaseResponse.Success) {
                emit(BaseResponse.Success(apiResult.data))
            } else if (apiResult is BaseResponse.Error) {
                emit(BaseResponse.Error(apiResult.error))
            }
        }
}