package com.saulhervas.easychat.data.repository.backend.retrofit

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val currentRequest = chain.request()

        needInterceptor(currentRequest)

        val newRequest = if (needInterceptor(currentRequest)){
            currentRequest.newBuilder()
                .addHeader(
                    "Authorization",
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY4OCIsImlhdCI6MTcyMTU4MjcwMSwiZXhwIjoxNzI0MTc0NzAxfQ.sjQYajFwdKtcCDI7eQHlg5Kp54VCPpK4kicl-mAj_Jk"
                )
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