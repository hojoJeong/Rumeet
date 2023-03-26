package com.d204.rumeet.data.remote.dto.request.auth

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequestDto(
    @SerializedName("id")
    val id : Int,
    @SerializedName("refreshToken")
    val refreshTokenRequestDto: String
)