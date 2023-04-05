package com.d204.rumeet.data.remote.mapper

import com.d204.rumeet.data.remote.dto.response.chatting.ChattingCreateResponseDto
import com.d204.rumeet.domain.model.chatting.ChattingCreateModel

internal fun ChattingCreateResponseDto.toDomainModel() = ChattingCreateModel(
    roomId, userId, nickname, profile, noReadCnt
)