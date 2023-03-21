package com.d204.rumeet.data.remote.mapper

import com.d204.rumeet.data.remote.dto.response.auth.JWTResponse
import com.d204.rumeet.domain.model.auth.JWTModel

internal fun JWTResponse.toDomain() = JWTModel(
    userId = this.id,
    accessToken = this.accessToken ?: "",
    refreshToken = this.refreshToken ?: ""
)