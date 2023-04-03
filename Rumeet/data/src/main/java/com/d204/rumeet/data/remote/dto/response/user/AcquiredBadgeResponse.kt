package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class AcquiredBadgeResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("code")
    val code: Int,
    @SerializedName("date")
    val date: Long
)
