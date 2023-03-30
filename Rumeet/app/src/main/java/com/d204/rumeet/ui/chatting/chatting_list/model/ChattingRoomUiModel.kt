package com.d204.rumeet.ui.chatting.chatting_list.model

import com.d204.rumeet.domain.model.chatting.ChattingRoomModel

data class ChattingRoomUiModel(
    val roomId : Int,
    val noReadCnt : Int,
    val nickname : String,
    val profile : String,
    val content : String,
    val date : Long,
    val otherUserId : Int
)

fun ChattingRoomModel.toUiModel() = ChattingRoomUiModel(
    roomId = this.roomId,
    noReadCnt = this.noReadCnt,
    nickname = this.senderNickName,
    profile = this.senderProfileImg,
    content = this.lastContent,
    date = this.lastDate,
    otherUserId = this.userId
)