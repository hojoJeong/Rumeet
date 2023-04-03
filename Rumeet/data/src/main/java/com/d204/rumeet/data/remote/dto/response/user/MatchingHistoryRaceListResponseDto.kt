package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class MatchingHistoryRaceListResponseDto(
    @SerializedName("raceId")
    val raceId: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("mode")
    val mode: Int,
    @SerializedName("success")
    val success: Int,
    @SerializedName("date")
    val date: Long,
    @SerializedName("partnerName")
    val partnerName: String,
    @SerializedName("pace")
    val pace: Int,
    @SerializedName("time")
    val time: Int,
    @SerializedName("km")
    val km: Double,
    @SerializedName("heartRate")
    val heartRate: Int,
    @SerializedName("kcal")
    val kcal: Double,
    @SerializedName("polyline")
    val polyline: String,
)