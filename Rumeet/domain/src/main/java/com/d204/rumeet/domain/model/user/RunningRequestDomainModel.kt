package com.d204.rumeet.domain.model.user

data class RunningRequestDomainModel(
    val date: Long,
    val mode: Int,
    val partnerId: Int,
    val raceId: Int,
    val state: Int,
    val userId: Int
)
