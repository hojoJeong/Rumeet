package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class RunningRecordRaceListResponseDto(
    @SerializedName("data")
    val date: Long,
    @SerializedName("heartRate")
    val heartRate: Int,
    @SerializedName("kcal")
    val kcal: Double,
    @SerializedName("km")
    val km: Double,
    @SerializedName("mode")
    val mode: Int,
    @SerializedName("pace")
    val pace: Int,
    @SerializedName("polyline")
    val polyline: String,
    @SerializedName("raceId")
    val raceId: Int,
    @SerializedName("success")
    val success: Int,
    @SerializedName("time")
    val time: Long,
    @SerializedName("userId")
    val userId: Int
)