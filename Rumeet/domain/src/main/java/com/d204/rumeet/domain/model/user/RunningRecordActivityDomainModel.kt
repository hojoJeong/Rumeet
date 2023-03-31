package com.d204.rumeet.domain.model.user

data class RunningRecordActivityDomainModel(
    val raceId: Int,
    val userId: Int,
    val mode: Int,
    val time: Long,
    val distance: Double,
    val pace: Int,
    val heartRate: Int,
    val calorie: Double,
    val success: Int,
    val polyLine: String,
    val date: Long
)