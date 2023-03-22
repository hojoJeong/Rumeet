package com.d204.rumeet.data.remote.api

import com.d204.rumeet.data.remote.dto.request.auth.EmailLoginRequest
import com.d204.rumeet.data.remote.dto.response.BaseResponse
import com.d204.rumeet.data.remote.dto.response.auth.JWTResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AuthApiService {
    @POST("users/login")
    suspend fun login(
        @Body info : EmailLoginRequest
    ) : BaseResponse<JWTResponse>
}