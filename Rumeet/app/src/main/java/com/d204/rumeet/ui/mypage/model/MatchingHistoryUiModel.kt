package com.d204.rumeet.ui.mypage.model

import com.d204.rumeet.domain.model.user.MatchingHistoryDomainModel
import com.d204.rumeet.ui.mypage.MyPageAction

data class MatchingHistoryUiModel(
    val raceList: List<MatchingHistoryRaceUiModel>,
    val summaryData: MatchingHistorySummaryDataUiModel
)

fun MatchingHistoryDomainModel.toUi() = MatchingHistoryUiModel(
    raceList.map { it.toUiModel() },
    summaryData.toUiModel()
)