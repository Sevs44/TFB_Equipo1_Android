
package com.saulhervas.easychat.hilt

import com.saulhervas.easychat.domain.model.UserSingleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {
    @Provides
    @Singleton
    fun provideUser(): UserSingleton {
        return UserSingleton("","")
    }
}
