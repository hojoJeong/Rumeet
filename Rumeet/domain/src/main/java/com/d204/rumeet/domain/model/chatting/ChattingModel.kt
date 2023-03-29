package com.d204.rumeet.domain.model.chatting

data class ChattingModel(
    val roomId : Int,
    // 방을 만든 사람
    val user1 : Int,
    //방을 들어온 사람
    val user2 : Int,
    val createdAt : Long,
    // -1 : 종료, 0 : 진행
    val state : Int,
    val chat : List<ChattingMessageModel>
)

data class ChattingMessageModel(
    val roomId : Int,
    val toUserId : Int,
    val fromUserId : Int,
    val content : String,
    val createdAt: Long
)