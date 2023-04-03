package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class FriendRecommendListResponseDto(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profile")
    val profile: String,
    @SerializedName("userId")
    val userId: Int
)