package com.d204.rumeet.ui.chatting.model

import android.opengl.Visibility
import com.d204.rumeet.domain.model.chatting.ChattingMessageModel

data class ChattingMessageUiModel(
    val message: ChattingMessageModel,
    val profileVisibility: Boolean = false
)

fun ChattingMessageModel.toUiModel(visibility: Boolean) = ChattingMessageUiModel(
    message = this,
    profileVisibility = visibility
)

fun List<ChattingMessageModel>.toUiList() : List<ChattingMessageUiModel> {
    val list = mutableListOf<ChattingMessageUiModel>()
    this.forEachIndexed { index, chattingMessageModel ->
        list.add(
            chattingMessageModel.toUiModel(
                if (index != 0) {
                    this[index - 1].fromUserId != this[index].fromUserId
                } else {
                    true
                }
            )
        )
    }
    return list.toList()
}