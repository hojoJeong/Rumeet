package com.d204.rumeet.data.remote.dto.response.user

import com.d204.rumeet.domain.model.user.RunningRecordActivityDomainModel
import com.google.gson.annotations.SerializedName

data class RunningRecordResponseDto(
    @SerializedName("raceList")
    val raceList: List<RunningRecordRaceListResponseDto>?,

    @SerializedName("summaryData")
    val summaryData: RunningRecordSummaryResponseDto?
)