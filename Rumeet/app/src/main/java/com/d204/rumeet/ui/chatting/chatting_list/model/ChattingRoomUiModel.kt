package com.d204.rumeet.ui.chatting.chatting_list.model

import com.d204.rumeet.domain.model.chatting.ChattingRoomModel

data class ChattingRoomUiModel(
    val roomId : Int,
    val noReadCount : Int,
    val senderNickName : String,
    val senderProfileImg : String,
    val lastContent : String,
    val lastDate : Long
)

fun ChattingRoomModel.toUiModel() = ChattingRoomUiModel(
    roomId = this.roomId,
    noReadCount = this.noReadCnt,
    senderNickName = this.senderNickName,
    senderProfileImg = this.senderProfileImg,
    lastContent = this.lastContent,
    lastDate = this.lastDate
)