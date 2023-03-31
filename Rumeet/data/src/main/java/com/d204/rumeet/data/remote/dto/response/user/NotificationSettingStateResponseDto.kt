package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class NotificationSettingStateResponseDto(
    @SerializedName("friendAlarm")
    val friendAlarm: Int,

    @SerializedName("matchingAlarm")
    val matchingAlarm: Int

)
