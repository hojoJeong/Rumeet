package com.d204.rumeet.domain.model.user

data class RunningRecordSummaryDomainModel(
    val totalDistance: Int,
    val totalTime: Long,
    val averagePace: Int
)