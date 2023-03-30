package com.d204.rumeet.ui.mypage.model

import com.d204.rumeet.domain.model.user.UserInfoDomainModel

data class UserInfoUiModel(
    val age: Int,
    val email: String,
    val gender: Int,
    val height: Double,
    val nickname: String = "닉네임",
    val oauth: String,
    val password: String?,
    val profile: String?,
    val weight: Double
)

fun UserInfoDomainModel.toUiModel() = UserInfoUiModel(
    age,
    email,
    gender,
    height,
    nickname,
    oauth,
    password,
    profile,
    weight
)