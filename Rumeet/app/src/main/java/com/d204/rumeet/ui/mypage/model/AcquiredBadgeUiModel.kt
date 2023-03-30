package com.d204.rumeet.ui.mypage.model

import com.d204.rumeet.domain.model.user.AcquiredBadgeListDomainModel

data class AcquiredBadgeUiModel(
    val id: Int,
    val code: Int,
    val date: Long
)

fun AcquiredBadgeListDomainModel.toUiModel() = AcquiredBadgeUiModel(
    id, code, date
)