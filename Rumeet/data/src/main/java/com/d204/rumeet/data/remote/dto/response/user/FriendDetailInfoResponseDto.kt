package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class FriendDetailInfoResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImg")
    val profileImg: String,
    @SerializedName("totalKm")
    val totalKm: Double,
    @SerializedName("totalTime")
    val totalTime: Long,
    @SerializedName("pace")
    val pace: Int
)