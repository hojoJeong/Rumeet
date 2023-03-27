package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.auth.JWTModel
import com.d204.rumeet.domain.model.auth.KakaoOAuthModel

interface AuthRepository {
    suspend fun getUserAutoLoginCheck() : Boolean
    suspend fun setUserAutoLoginCheck(state : Boolean) : Boolean
    suspend fun doEmailLogin(email : String, password : String, autoLoginState : Boolean) : NetworkResult<JWTModel>
    suspend fun doKakaoLogin(accessToken: String) : NetworkResult<JWTModel>
    suspend fun setUserToken(accessToken: String, refreshToken : String, userId : Int)  : Boolean
    suspend fun redirectKakaoLogin(accessToken: String) : NetworkResult<KakaoOAuthModel>
}