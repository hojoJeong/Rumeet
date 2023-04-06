package com.d204.rumeet.domain.model.user

data class RunningRecordDomainModel(
    val raceList: List<RunningRecordActivityDomainModel>,
    val summaryData: RunningRecordSummaryDomainModel?
)