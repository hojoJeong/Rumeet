package com.d204.rumeet.ui.mypage.model

import com.d204.rumeet.domain.model.user.MatchingHistoryRaceListDomainModel
import com.d204.rumeet.util.*

data class MatchingHistoryRaceUiModel(
    val date: String,
    val heartRate: Int,
    val kcal: Double,
    val km: String,
    val mode: String,
    val pace: String,
    val partnerName: String,
    val polyline: String,
    val raceId: Int,
    val success: String,
    val time: String,
    val userId: Int
)

fun MatchingHistoryRaceListDomainModel.toUiModel() = MatchingHistoryRaceUiModel(
    date.toDate(),
    heartRate,
    kcal,
    km.toDistance(),
    mode.toMode(),
    pace.toRecord(),
    partnerName,
    polyline,
    raceId,
    success.toSuccess(),
    time.toRecord(),
    userId
)