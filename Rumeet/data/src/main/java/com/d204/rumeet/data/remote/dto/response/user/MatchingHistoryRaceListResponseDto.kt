package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class MatchingHistoryRaceListResponseDto(
    @SerializedName("date")
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
    @SerializedName("partnerName")
    val partnerName: String,
    @SerializedName("polyline")
    val polyline: String,
    @SerializedName("raceId")
    val raceId: Int,
    @SerializedName("success")
    val success: Int,
    @SerializedName("time")
    val time: Int,
    @SerializedName("userId")
    val userId: Int
)