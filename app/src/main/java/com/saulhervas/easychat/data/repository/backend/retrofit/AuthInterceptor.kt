package com.saulhervas.easychat.data.repository.backend.retrofit

import android.util.Log
import com.saulhervas.easychat.domain.model.UserSession
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val userSession: UserSession): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentRequest = chain.request()
        Log.i("TAG", "intercept: user ==> $userSession")

        val newRequest = if (needInterceptor(currentRequest)){
            currentRequest.newBuilder()
                .addHeader(
                    "Authorization",
                    userSession.token)
                .build()
        } else {
            currentRequest
        }

        return chain.proceed(newRequest)
    }

    private fun needInterceptor(
        currentRequest: Request
    ): Boolean {
        val url = currentRequest.url.toString()
        return when {
            url.endsWith("users/login") -> false
            url.endsWith("users/register") -> false
            else -> true
        }
    }
}