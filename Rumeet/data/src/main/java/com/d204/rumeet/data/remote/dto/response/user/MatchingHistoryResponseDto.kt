package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class MatchingHistoryResponseDto(
    @SerializedName("raceList")
    val raceList: List<MatchingHistoryRaceListResponseDto>,

    @SerializedName("summaryData")
    val summaryData: MatchingHistorySummaryDataResponseDto
)