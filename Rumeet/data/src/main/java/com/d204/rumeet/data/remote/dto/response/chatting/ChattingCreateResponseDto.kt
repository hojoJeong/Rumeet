package com.d204.rumeet.data.remote.dto.response.chatting

import com.google.gson.annotations.SerializedName

data class ChattingCreateResponseDto(
    @SerializedName("roomId")
    val roomId: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profile")
    val profile: String,
    @SerializedName("noReadCnt")
    val noReadCnt: Int
)
