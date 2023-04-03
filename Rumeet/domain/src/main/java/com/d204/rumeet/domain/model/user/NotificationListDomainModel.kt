package com.d204.rumeet.domain.model.user

data class NotificationListDomainModel(
    val date: Long,
    val fromUserId: Int,
    val id: String,
    val toUserId: Int,
    val fromUserName: String,
    val fromUserProfile: String
)