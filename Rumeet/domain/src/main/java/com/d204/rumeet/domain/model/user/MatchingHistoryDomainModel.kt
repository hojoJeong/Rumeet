package com.d204.rumeet.domain.model.user

data class MatchingHistoryDomainModel(
    val raceList: List<MatchingHistoryRaceListDomainModel>,
    val summaryData: MatchingHistorySummaryDataDomainModel
)
