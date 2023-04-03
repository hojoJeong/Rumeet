package com.d204.rumeet.data.remote.dto.response.user

import com.d204.rumeet.domain.model.user.HomeBadgeDomainModel
import com.google.gson.annotations.SerializedName

data class HomeDataResponseDto(
    @SerializedName("badge")
    val badgeList: List<HomeBadgeResponseDto>,
    @SerializedName("record")
    val record: HomeRecordResponseDto?
)