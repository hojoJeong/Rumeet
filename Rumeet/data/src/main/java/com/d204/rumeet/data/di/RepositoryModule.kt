package com.d204.rumeet.data.di

import com.d204.rumeet.data.repository.AuthRepositoryImpl
import com.d204.rumeet.data.repository.SignRepositoryImpl
import com.d204.rumeet.data.repository.UserRepositoryImpl
import com.d204.rumeet.domain.repository.AuthRepository
import com.d204.rumeet.domain.repository.SignRepository
import com.d204.rumeet.domain.repository.UserRepository
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
}