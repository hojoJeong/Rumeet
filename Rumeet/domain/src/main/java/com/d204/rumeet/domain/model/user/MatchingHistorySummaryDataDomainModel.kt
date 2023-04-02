package com.d204.rumeet.domain.model.user

data class MatchingHistorySummaryDataDomainModel(
    val fail: Int,
    val matchCount: Int,
    val success: Int,
    val userId: Int
)