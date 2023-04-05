package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class FriendListResponseDto(
    @SerializedName("latestDate")
    val latestDate: Long,
    @SerializedName("matchCount")
    val matchCount: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("pace")
    val pace: Int,
    @SerializedName("profileImg")
    val profileImage: String,
    @SerializedName("userId")
    val userId: Int
)