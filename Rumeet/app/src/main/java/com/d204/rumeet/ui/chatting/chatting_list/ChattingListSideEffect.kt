package com.d204.rumeet.ui.chatting.chatting_list

import com.d204.rumeet.domain.model.chatting.ChattingRoomModel
import com.d204.rumeet.ui.chatting.chatting_list.adapter.ChattingListAdapter
import com.d204.rumeet.ui.chatting.chatting_list.model.ChattingRoomUiModel

sealed class ChattingListSideEffect {
    class SuccessGetChattingList(val isEmpty : Boolean) : ChattingListSideEffect()
    class NavigateChattingRoom(val chattingRoomId : Int, val profile : String, val otherUserId : Int) : ChattingListSideEffect()
    class SuccessNewChattingList(val chattingRoomInfo : List<ChattingRoomUiModel>) : ChattingListSideEffect()
}