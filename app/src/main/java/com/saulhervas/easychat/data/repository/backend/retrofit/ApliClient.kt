package com.saulhervas.easychat.data.repository.backend.retrofit

import com.google.gson.Gson
import com.saulhervas.easychat.data.repository.response.error.ErrorResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object ApiClient {

    private const val BASE_URL = "https://mock-movilidad.vass.es/chatvass/api/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(AuthInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
    fun parseError(response: Response<*>): ErrorResponse? {
        val gson = Gson()
        return try {
            response.errorBody()?.string()?.let {
                gson.fromJson(it, ErrorResponse::class.java)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}