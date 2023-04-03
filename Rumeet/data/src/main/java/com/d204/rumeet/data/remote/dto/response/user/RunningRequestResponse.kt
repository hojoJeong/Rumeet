package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class RunningRequestResponse(
    @SerializedName("date")
    val date: Long,
    @SerializedName("mode")
    val mode: Int,
    @SerializedName("partnerId")
    val partnerId: Int,
    @SerializedName("raceId")
    val raceId: Int,
    @SerializedName("state")
    val state: Int,
    @SerializedName("userId")
    val userId: Int
)