package com.saulhervas.easychat.data.repository.backend.retrofit

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.saulhervas.easychat.R
import com.saulhervas.easychat.data.repository.response.error.ErrorResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import retrofit2.Response

abstract class BaseService(private val context: Context) {

    suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): BaseResponse<T> {
        val response: Response<T>

        try {
            response = call.invoke()

            return if (!response.isSuccessful) {
                val errorResponse = mapErrorResponse(response)
                Log.e("TAG", "l> errorResponse: ${errorResponse.message}")
                BaseResponse.Error(errorResponse)
            } else {
                response.body()?.let { body ->
                    BaseResponse.Success(body)
                } ?: BaseResponse.Error(mapErrorResponse(response))
            }
        } catch (throwable: Throwable) {
            Log.e("TAG", "l> throwable: ${throwable.message}")
            throwable.printStackTrace()
            return BaseResponse.Error(mapErrorResponse(throwable))
        }
    }

    private fun <T> mapErrorResponse(response: Response<T>): ErrorResponse {
        val errorBody = response.errorBody()?.string()
        val errorData = try {
            val parsedData = Gson().fromJson(errorBody, ErrorResponse::class.java)
            if (response.code() == 401) {
                parsedData.message = 401.toString()
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
        return ErrorResponse(
            context.getString(R.string.string_errorresponsetoken),
            context.getString(R.string.errorResponseTryBefore)
        )
    }
}
