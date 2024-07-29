package com.saulhervas.easychat.data.repository.backend.retrofit

import com.saulhervas.easychat.domain.model.UserSession
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.net.ssl.HostnameVerifier

class ApiClient @Inject constructor(userSession: UserSession) {
    val retrofit: Retrofit

    companion object {
        private const val BASE_URL = "https://mock-movilidad.vass.es/chatvass/api/"
        private const val RETROFIT_TIMEOUT_IN_SECOND = 5L
        private const val BASE_DOMAIN_MOCK_VASS_NAME = "mock-movilidad.vass.es"
        private const val SHA_MOCK_VASS_KEY = "sha256/38MULvZkNfG+CaKdXdEJydgSq3PMoT0JB8FkX/ossBw="
    }

    init {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

        val certificatePinner = CertificatePinner.Builder()
            .add(BASE_DOMAIN_MOCK_VASS_NAME, SHA_MOCK_VASS_KEY)
            .build()
        httpClient.certificatePinner(certificatePinner)


        val hostNamesAllow = listOf(BASE_DOMAIN_MOCK_VASS_NAME)
        val hostNameVerifier = HostnameVerifier { hostname, _ ->
            hostname in hostNamesAllow
        }
        httpClient.hostnameVerifier(hostNameVerifier)

        httpClient
            .connectTimeout(RETROFIT_TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
            .readTimeout(RETROFIT_TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
            .writeTimeout(RETROFIT_TIMEOUT_IN_SECOND, TimeUnit.SECONDS)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        httpClient
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(userSession))
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()

    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}