package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.chatting.ChattingRoomModel

interface ChattingRepository {
    suspend fun getChattingRooms(userId : Int) : NetworkResult<List<ChattingRoomModel>>
}