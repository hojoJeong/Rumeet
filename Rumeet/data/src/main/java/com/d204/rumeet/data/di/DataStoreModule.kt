package com.d204.rumeet.data.di

import android.content.Context
import com.d204.rumeet.data.local.sharedpreference.UserDataStorePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideUserDataSource(
        @ApplicationContext context: Context
    ) = UserDataStorePreferences(context)
}