package com.d204.rumeet.ui.chatting.chatting_list

interface ChattingRoomClickListener {
    fun onChattingRoomClick(roomId: Int, profile: String, otherUserId : Int, noReadCnt : Int)
}