package com.d204.rumeet.domain.model.friend

data class RequestFriendDomainModel(
    val fromUserId: Int,
    val toUserId: Int
)