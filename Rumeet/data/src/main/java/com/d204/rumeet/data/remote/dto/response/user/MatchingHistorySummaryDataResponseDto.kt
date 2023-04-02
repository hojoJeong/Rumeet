package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class MatchingHistorySummaryDataResponseDto(
    @SerializedName("fail")
    val fail: Int,
    @SerializedName("matchCount")
    val matchCount: Int,
    @SerializedName("Success")
    val success: Int,
    @SerializedName("userId")
    val userId: Int
)