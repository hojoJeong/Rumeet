package com.d204.rumeet.data.remote.dto.request.friend

import com.google.gson.annotations.SerializedName

data class FriendRequestDto (
    @SerializedName("fromUserId")
    val fromUserId : Int,
    @SerializedName("toUserId")
    val toUserId : Int
)