package com.d204.rumeet.ui.chatting

import com.d204.rumeet.domain.model.chatting.ChattingMessageModel

sealed class ChattingSideEffect{
    object RequestSendMessage : ChattingSideEffect()
    object StartSubscribeRabbiMq : ChattingSideEffect()
    object SendChatting: ChattingSideEffect()
    class ReceiveChatting(val messageModel : ChattingMessageModel) : ChattingSideEffect()
}
