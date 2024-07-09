package com.saulhervas.easychat.hilt

import com.saulhervas.easychat.data.repository.backend.retrofit.ApiClient
import com.saulhervas.easychat.data.repository.backend.retrofit.ApiService
import com.saulhervas.easychat.data.repository.backend.retrofit.BaseApiService
import com.saulhervas.easychat.data.repository.backend.retrofit.ChatsDataSource
import com.saulhervas.easychat.data.repository.backend.retrofit.OpenChatsDataProvider
import com.saulhervas.easychat.domain.usecases.OpenChatUseCases
import com.saulhervas.easychat.ui.home.HomeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Singleton
    @Provides
    fun provideHomeViewModel(openChatUseCases: OpenChatUseCases): HomeViewModel {
        return HomeViewModel(openChatUseCases)
    }

    @Singleton
    @Provides
    fun provideOpenChatUseCases(dataProvider: OpenChatsDataProvider): OpenChatUseCases {
        return OpenChatUseCases(dataProvider)
    }

    @Singleton
    @Provides
    fun provideOpenChatsDataProvider(remoteDataSource: ChatsDataSource): OpenChatsDataProvider {
        return OpenChatsDataProvider(remoteDataSource)
    }

    @Singleton
    @Provides
    fun provideChatsDataSource(baseApiService: BaseApiService): ChatsDataSource {
        return ChatsDataSource(baseApiService)
    }

    @Singleton
    @Provides
    fun provideBaseApiService(apiService: ApiService): BaseApiService {
        return BaseApiService(apiService)
    }

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return ApiClient.create(ApiService::class.java)
    }
}