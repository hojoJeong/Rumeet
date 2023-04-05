package com.d204.rumeet.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.d204.rumeet.data.remote.api.ChattingApiService
import com.d204.rumeet.data.remote.api.handleApi
import com.d204.rumeet.data.remote.dto.request.chatting.ChattingCreateRequestDto
import com.d204.rumeet.data.remote.dto.response.chatting.ChattingCreateResponseDto
import com.d204.rumeet.data.remote.dto.response.chatting.ChattingResponseDto
import com.d204.rumeet.data.remote.dto.response.chatting.ChattingRoomResponseDto
import com.d204.rumeet.data.remote.dto.response.chatting.toDomainModel
import com.d204.rumeet.data.remote.mapper.toDomainModel
import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.chatting.ChattingCreateModel
import com.d204.rumeet.domain.model.chatting.ChattingModel
import com.d204.rumeet.domain.model.chatting.ChattingRoomModel
import com.d204.rumeet.domain.repository.ChattingRepository
import com.d204.rumeet.domain.toDomainResult
import javax.inject.Inject

internal class ChattingRepositoryImpl @Inject constructor(
    private val chattingApiService: ChattingApiService
) : ChattingRepository {
    override suspend fun getChattingRooms(userId: Int): NetworkResult<List<ChattingRoomModel>> {
        return handleApi { chattingApiService.getChattingRoom(userId) }
            .toDomainResult<List<ChattingRoomResponseDto>, List<ChattingRoomModel>> { response ->
                response.map {
                    it.toDomainModel()
                }
            }
    }

    override suspend fun getChattingInfo(roomId: Int): NetworkResult<ChattingModel> {
        return handleApi { chattingApiService.getChattingList(roomId) }.toDomainResult<ChattingResponseDto, ChattingModel> { it.toDomainModel() }
    }

    override suspend fun createChattingRoom(
        userId: Int,
        friendId: Int
    ): NetworkResult<ChattingCreateModel> {
        val request = ChattingCreateRequestDto(userId, friendId)
        val response = handleApi { chattingApiService.createChattingRoom(request) }.toDomainResult<ChattingCreateResponseDto, ChattingCreateModel> { it.toDomainModel() }
        Log.d(TAG, "createChattingRoom: $response")
        return response
    }
}