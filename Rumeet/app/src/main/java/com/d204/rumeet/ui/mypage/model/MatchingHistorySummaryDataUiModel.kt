package com.d204.rumeet.ui.mypage.model

import com.d204.rumeet.domain.model.user.MatchingHistorySummaryDataDomainModel
import com.d204.rumeet.util.toCount

data class MatchingHistorySummaryDataUiModel(
    val fail: String,
    val matchCount: String,
    val success: String,
    val userId: Int
)

fun MatchingHistorySummaryDataDomainModel.toUiModel() = MatchingHistorySummaryDataUiModel(
    fail.toCount(), matchCount.toCount(), success.toCount(), userId
)
