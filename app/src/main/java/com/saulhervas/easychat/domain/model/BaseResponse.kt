package com.saulhervas.easychat.domain.model

import com.saulhervas.easychat.data.model.modelslogin.ErrorResponse

sealed class BaseResponse<T> {
    class Success<T>(val data: T) : BaseResponse<T>()
    class Error<T>(val error: ErrorResponse) : BaseResponse<T>()
}