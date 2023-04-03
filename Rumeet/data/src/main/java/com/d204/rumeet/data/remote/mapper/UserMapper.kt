package com.d204.rumeet.data.remote.mapper

import com.d204.rumeet.data.remote.dto.response.user.UserInfoResponse
import com.d204.rumeet.domain.model.user.UserInfoDomainModel

internal fun UserInfoResponse.toDomainModel() = UserInfoDomainModel(
    age ?: 0,
    date ?: 0,
    email ?: "",
    gender ?: -1,
    height ?: 0.0,
    nickname ?: "",
    oauth ?: "",
    password ?: "",
    profile ?: "",
    refreshToken ?: "",
    state ?: -1,
    weight ?: 0.0
)