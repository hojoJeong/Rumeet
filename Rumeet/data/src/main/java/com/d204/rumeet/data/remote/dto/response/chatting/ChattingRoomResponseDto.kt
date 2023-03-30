package com.d204.rumeet.data.remote.dto.response.chatting

import com.d204.rumeet.domain.model.chatting.ChattingRoomModel
import com.google.gson.annotations.SerializedName

internal data class ChattingRoomResponseDto(
    @SerializedName("roomId")
    val roomId : Int,
    @SerializedName("userId")
    val userId : Int,
    @SerializedName("noReadCnt")
    val noReadCnt : Int,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("profile")
    val sendUserProfileImg : String?,
    @SerializedName("content")
    val lastContent : String,
    @SerializedName("date")
    val lastDate : Long
)


internal fun ChattingRoomResponseDto.toDomainModel() = ChattingRoomModel(
    roomId = this.roomId,
    userId = this.userId,
    noReadCnt = this.noReadCnt,
    senderNickName = this.nickname,
    senderProfileImg = this.sendUserProfileImg ?: "",
    lastContent = this.lastContent,
    lastDate = this.lastDate
)