package com.d204.rumeet.ui.friend_list.model

import com.d204.rumeet.domain.model.user.FriendModel

data class FriendInfoModel(
    val userId : Int,
    val userProfileImg : String,
    val userNickname : String,
    val averagePace : String = ""
)

fun FriendModel.toUiModel() = FriendInfoModel(
    userId = this.userId,
    userProfileImg = "",
    userNickname = "",
    averagePace = ""
)