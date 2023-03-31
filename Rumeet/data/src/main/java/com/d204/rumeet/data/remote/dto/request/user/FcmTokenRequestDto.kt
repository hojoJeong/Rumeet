package com.d204.rumeet.data.remote.dto.request.user

import com.google.gson.annotations.SerializedName

data class FcmTokenRequestDto(
    @SerializedName("userId")
    val userId: Int,

    @SerializedName("fcmToken")
    val token: String
)
