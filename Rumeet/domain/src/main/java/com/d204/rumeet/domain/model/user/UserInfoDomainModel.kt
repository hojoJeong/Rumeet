package com.d204.rumeet.domain.model.user

data class UserInfoDomainModel(
    val age: Int,
    val date: Long,
    val email: String,
    val gender: Int,
    val height: Double,
    val nickname: String,
    val oauth: String,
    val password: String?,
    val profile: String?,
    val refreshToken: String,
    val state: Int,
    val weight: Double
)