package com.d204.rumeet.ui.mypage.model

import com.d204.rumeet.domain.model.user.RunningRecordActivityDomainModel
import com.d204.rumeet.util.*

data class RunningActivityUiModel(
    val raceId: Int,
    val userId: Int,
    val mode: String,
    val time: String,
    val distance: String,
    val pace: String,
    val heartRate: Int,
    val calorie: Double,
    val success: String,
    val polyLine: String,
    val date: String
)

fun RunningRecordActivityDomainModel.toUiModel() = RunningActivityUiModel(
    raceId,
    userId,
    mode.toMode(),
    time.toMinute(),
    "${distance}km",
    pace.toRecord(),
    heartRate,
    calorie,
    success.toSuccess(),
    polyLine,
    date.toDate()
)