package com.d204.rumeet.data.di

import com.d204.rumeet.data.repository.*
import com.d204.rumeet.data.repository.AuthRepositoryImpl
import com.d204.rumeet.data.repository.FriendRepositoryImpl
import com.d204.rumeet.data.repository.SignRepositoryImpl
import com.d204.rumeet.data.repository.UserRepositoryImpl
import com.d204.rumeet.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsUserRepositoryImpl(
        userRepositoryImpl: UserRepositoryImpl
    ) : UserRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepositoryImpl(
        authRepositoryImpl: AuthRepositoryImpl
    ) : AuthRepository

    @Binds
    @Singleton
    abstract fun bindSignRepositoryImpl(
        signRepositoryImpl: SignRepositoryImpl
    ) : SignRepository

    @Binds
    @Singleton
    abstract fun bindFriendRepositoryImpl(
        friendRepositoryImpl : FriendRepositoryImpl
    ) : FriendRepository

    @Binds
    @Singleton
    abstract fun bindChattingRepositoryImpl(
        chattingRepositoryImpl: ChattingRepositoryImpl
    ) : ChattingRepository
}