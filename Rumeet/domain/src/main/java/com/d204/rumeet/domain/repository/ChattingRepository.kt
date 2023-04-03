package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.chatting.ChattingCreateModel
import com.d204.rumeet.domain.model.chatting.ChattingModel
import com.d204.rumeet.domain.model.chatting.ChattingRoomModel

interface ChattingRepository {
    suspend fun getChattingRooms(userId : Int) : NetworkResult<List<ChattingRoomModel>>
    suspend fun getChattingInfo(roomId : Int) : NetworkResult<ChattingModel>
    suspend fun createChattingRoom(userId: Int, friendId: Int): NetworkResult<ChattingCreateModel>
}