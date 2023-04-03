package com.d204.rumeet.data.remote.dto.request.user

import com.google.gson.annotations.SerializedName

data class ModifyNotificationStateRequestDto(
    @SerializedName("userId")
    val userId: Int,

    @SerializedName("target")
    val target: Int,

    @SerializedName("state")
    val state: Int
)