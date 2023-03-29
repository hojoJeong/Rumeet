package com.d204.rumeet.data.remote.dto.response.chatting

import com.d204.rumeet.domain.model.chatting.ChattingMessageModel
import com.d204.rumeet.domain.model.chatting.ChattingModel
import com.google.gson.annotations.SerializedName


internal data class ChattingResponseDto(
    @SerializedName("id")
    val roomId : Int,
    // 방을 만든 사람
    @SerializedName("user1")
    val user1 : Int,
    //방을 들어온 사람
    @SerializedName("user2")
    val user2 : Int,
    @SerializedName("date")
    val createdAt: Long,
    // -1 : 종료, 0 : 진행
    @SerializedName("state")
    val state : Int,
    @SerializedName("chat")
    val chat : List<ChattingMessageResponseDto>
)

internal data class ChattingMessageResponseDto(
    @SerializedName("roomId")
    val roomId : Int,
    @SerializedName("toUserId")
    val toUserId : Int,
    @SerializedName("fromUserId")
    val fromUserId : Int,
    @SerializedName("content")
    val content : String,
    @SerializedName("date")
    val createdAt: Long
)

internal fun ChattingResponseDto.toDomainModel() = ChattingModel(
    roomId = this.roomId,
    user1 = this.user1,
    user2 = this.user2,
    createdAt = this.createdAt,
    state = this.state,
    chat = this.chat.map { it.toDomainModel() }
)

internal fun ChattingMessageResponseDto.toDomainModel() = ChattingMessageModel(
    roomId = this.roomId,
    toUserId = this.toUserId,
    fromUserId = this.fromUserId,
    content = this.content,
    createdAt = this.createdAt
)