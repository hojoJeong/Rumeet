package com.d204.rumeet.data.remote.dto.request.running

import com.google.gson.annotations.SerializedName

data class RunningMatchingWithFriendRequestDto(
    @SerializedName("date")
    val date: Long,
    @SerializedName("mode")
    val mode: Int,
    @SerializedName("partnerId")
    val partnerId: Int,
    @SerializedName("userId")
    val userId: Int
)