package com.d204.rumeet.ui.mypage.model

data class UserInfoUiModel(
    val age: Int,
    val date: Any,
    val email: String,
    val gender: Int,
    val height: Double,
    val id: Int,
    val nickname: String = "닉네임",
    val oauth: Any,
    val password: String?,
    val profileImg: Any,
    val refreshToken: Any,
    val state: Int,
    val weight: Double
)