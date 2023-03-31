package com.d204.rumeet.domain.model.user

data class HomeRecordDomainModel(
    val averagePace: Int? = 0,
    val nickname: String? = "",
    val totalCount: Int? = 0,
    val totalKm: Int? = 0,
    val userId: Int? = 0
)