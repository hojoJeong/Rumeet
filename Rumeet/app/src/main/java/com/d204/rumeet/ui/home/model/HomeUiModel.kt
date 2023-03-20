package com.d204.rumeet.ui.home.model

data class HomeUiModel(
    val userName: String,
    val bestRecordList: BestRecordUiModel,
    val badgeList: List<String>,
    val recommendFriendList: List<RecommendFriendUiModel>
)