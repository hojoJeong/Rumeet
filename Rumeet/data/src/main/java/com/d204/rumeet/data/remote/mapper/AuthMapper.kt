package com.d204.rumeet.data.remote.mapper

import com.d204.rumeet.data.remote.dto.response.auth.JWTResponse
import com.d204.rumeet.data.remote.dto.response.auth.KakaoOAuthResponse
import com.d204.rumeet.domain.model.auth.JWTModel
import com.d204.rumeet.domain.model.auth.KakaoOAuthModel

internal fun JWTResponse.toDomain() = JWTModel(
    userId = this.id,
    accessToken = this.accessToken ?: "",
    refreshToken = this.refreshToken ?: ""
)

internal fun KakaoOAuthResponse.toDomain() = KakaoOAuthModel(
    oauth = this.oauth,
    profileImg = this.profileImg ?: ""
)