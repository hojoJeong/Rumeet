package com.d204.rumeet.ui.mypage.model

data class UserInfoUiModel(
    val age: Int,
    val date: Long,
    val email: String,
    val gender: Int,
    val height: Double,
    val id: Int,
    val nickname: String = "닉네임",
    val oauth: String,
    val password: String?,
    val profileImg: String?,
    val refreshToken: String,
    val state: Int,
    val weight: Double
)