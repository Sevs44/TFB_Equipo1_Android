package com.saulhervas.easychat.hilt

import com.saulhervas.easychat.data.repository.backend.retrofit.ApiClient
import com.saulhervas.easychat.data.repository.backend.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiClient.create(ApiService::class.java)
    }
}