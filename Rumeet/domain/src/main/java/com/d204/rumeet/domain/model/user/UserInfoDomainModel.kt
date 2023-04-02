package com.d204.rumeet.domain.model.user

data class UserInfoDomainModel(
    val email: String,
    val password: String,
    val nickname: String,
    val age: Int,
    val gender: Int,
    val profile: String?,
    val height: Double,
    val weight: Double,
    val date: Long,
    val state: Int,
    val oauth: String?
)