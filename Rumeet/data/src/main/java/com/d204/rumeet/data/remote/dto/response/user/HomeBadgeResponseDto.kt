package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class HomeBadgeResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val date: Long
)