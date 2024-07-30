package com.saulhervas.easychat.data.repository.backend.retrofit

import android.util.Log
import com.google.gson.Gson
import com.saulhervas.easychat.R
import com.saulhervas.easychat.data.repository.response.error.ErrorResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.ui.main.MainActivity
import retrofit2.Response

abstract class BaseService {

    suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): BaseResponse<T> {
        val response: Response<T>

        try {
            response = call.invoke()

            return if (!response.isSuccessful) {
                val errorResponse = mapErrorResponse(response)
                BaseResponse.Error(errorResponse)
            } else {
                response.body()?.let { body ->
                    BaseResponse.Success(body)
                } ?: BaseResponse.Error(mapErrorResponse(response))
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            return BaseResponse.Error(mapErrorResponse(throwable))
        }
    }

    private fun <T> mapErrorResponse(response: Response<T>): ErrorResponse {
        val errorBody = response.errorBody()?.string()
        val errorData = try {
            val parsedData = Gson().fromJson(errorBody, ErrorResponse::class.java)
            val context = MainActivity.getAppContext()
            when (response.code()) {
                101 -> parsedData.message = context.getString(R.string.error_response_101)
                401 -> parsedData.message = context.getString(R.string.error_response_401)
                403 -> parsedData.message = context.getString(R.string.error_response_403)
                404 -> parsedData.message = context.getString(R.string.error_response_404)
                in 500..600 -> parsedData.message = context.getString(R.string.error_response_5xx)
                else -> parsedData.message =
                    context.getString(R.string.error_response_base, response.code().toString())
            }
            parsedData
        } catch (exception: java.lang.Exception) {
            Log.e("TAG", "l> exception: ${exception.message}")
            exception.printStackTrace()
            null
        }

        return ErrorResponse("", errorData?.message ?: "")
    }

    private fun mapErrorResponse(throwable: Throwable): ErrorResponse {
        Log.i("TAG", "mapErrorResponse: throwable ==> $throwable")
        val context = MainActivity.getAppContext()
        return ErrorResponse(
            context.getString(R.string.string_errorresponsetoken),
            context.getString(R.string.errorResponseTryBefore)
        )
    }
}
