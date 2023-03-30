package com.d204.rumeet.domain.model.chatting

data class ChattingRoomModel(
    val roomId : Int,
    val userId : Int,
    val noReadCnt : Int,
    val senderNickName : String,
    val senderProfileImg : String,
    val lastContent : String,
    val lastDate : Long
)
