package com.d204.rumeet.ui.chatting.chatting_list

import com.d204.rumeet.ui.chatting.chatting_list.adapter.ChattingListAdapter

sealed class ChattingListSideEffect {
    class SuccessGetChattingList(val isEmpty : Boolean) : ChattingListSideEffect()
    class NavigateChattingRoom(val chattingRoomId : Int, val profile : String) : ChattingListSideEffect()
}