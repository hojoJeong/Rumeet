package com.d204.rumeet.ui.chatting

import com.d204.rumeet.domain.model.chatting.ChattingMessageModel

sealed class ChattingSideEffect{
    class SuccessChattingData(val userId : Int) : ChattingSideEffect()
    object SendChatting: ChattingSideEffect()
    class ReceiveChatting(val messageModel : ChattingMessageModel?) : ChattingSideEffect()
}
