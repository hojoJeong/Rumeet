package com.d204.rumeet.domain.model.auth

data class JWTModel(
    val userId : Int,
    val accessToken : String,
    val refreshToken : String
)