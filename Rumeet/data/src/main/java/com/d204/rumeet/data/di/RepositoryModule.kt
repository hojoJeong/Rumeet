package com.d204.rumeet.data.di

import com.d204.rumeet.data.repository.UserRepositoryImpl
import com.d204.rumeet.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindsUserRepositoryImpl(
        userRepositoryImpl: UserRepositoryImpl
    ) : UserRepository
}