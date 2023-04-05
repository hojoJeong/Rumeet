package com.d204.rumeet.data.di

import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import com.d204.rumeet.data.remote.api.AuthApiService
import com.d204.rumeet.data.remote.interceptor.TokenAuthInterceptor
import com.d204.rumeet.data.remote.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    private const val BASE_URL = "http://j8d204.p.ssafy.io/rumeet/"
    private const val BASE_URL_3 = "http://192.168.137.166:8080/rumeet/"
    private const val BASE_URL_2 = "http://119.202.203.157/rumeet/"

    @Provides
    @Singleton
    @Named("NoAuthHttpClient")
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    @Provides
    @Singleton
    @Named("AuthHttpClient")
    fun provideAuthHttpClient(
        tokenAuthInterceptor: TokenAuthInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    @Singleton
    fun provideBearerInterceptor(
        authApiService: AuthApiService,
        userDataStorePreferences: UserDataStorePreferences
    ) : TokenAuthInterceptor{
        return TokenAuthInterceptor(authApiService, userDataStorePreferences)
    }

    @Provides
    @Singleton
    fun provideXAccessTokenInterceptor(
        userDataStorePreferences: UserDataStorePreferences
    ) : AuthInterceptor {
        return AuthInterceptor(userDataStorePreferences)
    }


    @Provides
    @Singleton
    @Named("NoAuth")
    fun provideRetrofit(
        @Named("NoAuthHttpClient") okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()


    @Provides
    @Singleton
    @Named("Auth")
    fun provideAuthRetrofit(
        @Named("AuthHttpClient") okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
}