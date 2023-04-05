package com.d204.rumeet.domain.model.friend

data class FriendInfoDomainModel(
    val id: Int,
    val nickname: String,
    val pace: Int,
    val profileImg: String,
    val totalKm: Int,
    val totalTime: Int
)