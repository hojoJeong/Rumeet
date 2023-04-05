package com.d204.rumeet.domain.model.friend

data class FriendListDomainModel(
    val latestDate: Long,
    val matchCount: Int,
    val nickname: String,
    val pace: Int,
    val profileImage: String,
    val userId: Int
)