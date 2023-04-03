package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class HomeRecordResponseDto(
    @SerializedName("averagePace")
    val averagePace: Int ? = 0,
    @SerializedName("nickname")
    val nickname: String ?= "",
    @SerializedName("totalCount")
    val totalCount: Int ?= 0,
    @SerializedName("totalKm")
    val totalKm: Int?=0,
    @SerializedName("userId")
    val userId: Int?=0
)