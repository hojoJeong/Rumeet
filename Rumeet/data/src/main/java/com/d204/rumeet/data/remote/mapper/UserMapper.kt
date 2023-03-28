package com.d204.rumeet.data.remote.mapper

import com.d204.rumeet.data.remote.dto.response.user.UserInfoResponse
import com.d204.rumeet.domain.model.user.UserInfoDomainModel

internal fun UserInfoResponse.toDomainModel() = UserInfoDomainModel(
    age,
    date,
    email,
    gender,
    height,
    id,
    nickname,
    oauth,
    password,
    profileImg?:"",
    refreshToken,
    state,
    weight
)