package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class NotificationListResponseDto(
    @SerializedName("date")
    val date: Long,
    @SerializedName("fromUserId")
    val fromUserId: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("toUserId")
    val toUserId: Int,
    @SerializedName("fromUserName")
    val fromUserName: String,
    @SerializedName("fromUserProfile")
    val fromUserProfile: String
)