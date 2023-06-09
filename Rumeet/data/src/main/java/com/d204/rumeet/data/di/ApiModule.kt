package com.d204.rumeet.data.di

import com.d204.rumeet.data.remote.api.*
import com.d204.rumeet.data.remote.api.AuthApiService
import com.d204.rumeet.data.remote.api.FriendApiService
import com.d204.rumeet.data.remote.api.SignApiService
import com.d204.rumeet.data.remote.api.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object ApiModule {

    @Provides
    @Singleton
    fun providesAuthApiService(
        @Named("NoAuth") retrofit: Retrofit
    ) : AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesSignApiService(
        @Named("Auth") retrofit: Retrofit
    ) : SignApiService{
        return retrofit.create(SignApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesUserApiService(
        @Named("Auth") retrofit: Retrofit
    ) : UserApiService{
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFriendApiService(
        @Named("Auth") retrofit : Retrofit
    ) : FriendApiService{
        return retrofit.create(FriendApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideChattingApiService(
        @Named("Auth") retrofit: Retrofit
    ) : ChattingApiService{
        return retrofit.create(ChattingApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRunningApiService(
        @Named("Auth") retrofit: Retrofit
    ) : RunningApiService{
        return retrofit.create(RunningApiService::class.java)
    }
}