package com.d204.rumeet.data.remote.api

import com.d204.rumeet.data.remote.dto.request.auth.EmailLoginRequest
import com.d204.rumeet.data.remote.dto.response.BaseResponse
import com.d204.rumeet.data.remote.dto.response.auth.JWTResponse
import com.d204.rumeet.data.remote.dto.response.auth.KakaoOAuthResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

internal interface AuthApiService {
    @POST("users/login")
    suspend fun login(
        @Body info : EmailLoginRequest
    ) : BaseResponse<JWTResponse>

    @GET("users/oauth/kakao")
    suspend fun kakaoLogin(
        @Query("code") accessToken : String
    ) : BaseResponse<JWTResponse>

    @GET("users/oauth/kakao")
    suspend fun getKakaoOauthInfo(
        @Query("code") accessToken: String
    ) : BaseResponse<KakaoOAuthResponse>
}