package com.d204.rumeet.data.repository

import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import com.d204.rumeet.data.remote.api.AuthApiService
import com.d204.rumeet.data.remote.api.handleApi
import com.d204.rumeet.data.remote.dto.request.auth.EmailLoginRequest
import com.d204.rumeet.data.remote.dto.response.auth.JWTResponse
import com.d204.rumeet.data.remote.mapper.toDomain
import com.d204.rumeet.domain.*
import com.d204.rumeet.domain.model.auth.JWTModel
import com.d204.rumeet.domain.repository.AuthRepository
import java.io.IOException
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val userDataStorePreferences : UserDataStorePreferences,
    private val authApiService: AuthApiService
) : AuthRepository {
    override suspend fun getUserAutoLoginCheck(): Boolean {
        return userDataStorePreferences.getAutoLogin()
    }

    override suspend fun setUserAutoLoginCheck(state: Boolean): Boolean {
        try {
            userDataStorePreferences.setAutoLogin(state)
        } catch (e: IOException) {
            return false
        }
        return true
    }
    override suspend fun doEmailLogin(email: String, password: String, autoLoginState: Boolean) : NetworkResult<JWTModel> {
        val request = EmailLoginRequest(email, password)
        val response = handleApi { authApiService.login(request) }
            .toDomainResult<JWTResponse, JWTModel> { it.toDomain() }

        // 예외가 발생한다면 setAutoLogin은 동작하지 않도록
        userDataStorePreferences.setAutoLogin(autoLoginState)
        return response
    }

    override suspend fun doKakaoLogin(accessToken: String) : NetworkResult<JWTModel> {
        TODO("Not yet implemented")
    }

    override suspend fun setUserToken(accessToken: String, refreshToken: String) : Boolean {
        try {
            userDataStorePreferences.setToken(accessToken, refreshToken)
        }catch (e : IOException){
            return false
        }
        return true
    }
}