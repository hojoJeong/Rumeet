package com.d204.rumeet.domain.model.user

data class RunningSoloDomainModel(
    val id: Int,
    val date: Long,
    val mode: Int,
    val pace: List<Int>? = emptyList(),
    val partnerId: Int,
    val state: Int,
    val userId: Int
)
