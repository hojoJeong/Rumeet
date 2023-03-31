package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class RunningRecordSummaryResponseDto(
    @SerializedName("total_km")
    val totalDistance: Int,
    @SerializedName("total_time")
    val totalTime: Long,
    @SerializedName("avg_pace")
    val averagePace: Int
)