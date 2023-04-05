package com.d204.rumeet.data.remote.dto.request.chatting

import com.google.gson.annotations.SerializedName

data class ChattingCreateRequestDto(
    @SerializedName("user1")
    val user1: Int,
    @SerializedName("user2")
    val user2: Int
)