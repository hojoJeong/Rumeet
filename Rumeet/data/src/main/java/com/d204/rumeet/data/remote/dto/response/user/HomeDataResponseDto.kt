package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class HomeDataResponseDto(
    @SerializedName("averagePace")
    val averagePace: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("totalCount")
    val totalCount: Int,
    @SerializedName("totalKm")
    val totalKm: Int,
    @SerializedName("userId")
    val userId: Int
)