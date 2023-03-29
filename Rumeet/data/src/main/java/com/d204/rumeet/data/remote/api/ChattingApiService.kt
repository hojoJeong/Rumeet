package com.d204.rumeet.data.remote.api

import com.d204.rumeet.data.remote.dto.response.BaseResponse
import com.d204.rumeet.data.remote.dto.response.chatting.ChattingMessageResponseDto
import com.d204.rumeet.data.remote.dto.response.chatting.ChattingResponseDto
import com.d204.rumeet.data.remote.dto.response.chatting.ChattingRoomResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ChattingApiService {
    @GET("chat/list/{userId}")
    suspend fun getChattingRoom(
        @Path("userId")userid : Int
    ) : BaseResponse<List<ChattingRoomResponseDto>>

    @GET("chat/{id}")
    suspend fun getChattingList(
        @Path("id") roomId : Int
    ) : BaseResponse<ChattingResponseDto>
}