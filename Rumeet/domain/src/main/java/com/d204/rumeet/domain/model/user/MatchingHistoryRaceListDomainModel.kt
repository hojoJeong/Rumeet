package com.d204.rumeet.domain.model.user

data class MatchingHistoryRaceListDomainModel(
    val date: Long,
    val heartRate: Int,
    val kcal: Double,
    val km: Int,
    val mode: Int,
    val pace: Int,
    val partnerName: String,
    val polyline: String,
    val raceId: Int,
    val success: Int,
    val time: Int,
    val userId: Int
)
