package com.d204.rumeet.data.remote.dto.request.running

import com.google.gson.annotations.SerializedName

internal data class RunningInfoRequestDto(
    @SerializedName("userId")
    val userId : Int,
    @SerializedName("raceId")
    val raceId : Int,
    val mode : Int,
    val velocity : Double,
    val time : Int,
    val heartRate : Int = 100,
    val success : Int
)
