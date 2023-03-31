package com.d204.rumeet.domain.model.user

data class HomeDataDomainModel(
    val averagePace: Int,
    val nickname: String,
    val totalCount: Int,
    val totalKm: Int,
    val userId: Int
)