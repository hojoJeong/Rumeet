package com.d204.rumeet.data.remote.dto.response.auth

import com.google.gson.annotations.SerializedName

internal data class JWTResponse(
    @SerializedName("id")
    val id : Int,
    @SerializedName("accessToken")
    val accessToken : String?,
    @SerializedName("refreshToken")
    val refreshToken : String?
)