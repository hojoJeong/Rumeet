package com.d204.rumeet.domain.model.user

data class UserInfoDomainModel(
    val age: Int,
    val date: Any,
    val email: String,
    val gender: Int,
    val height: Double,
    val id: Int,
    val nickname: String,
    val oauth: Any,
    val password: String,
    val profileImg: Any?,
    val refreshToken: Any,
    val state: Int,
    val weight: Double
)