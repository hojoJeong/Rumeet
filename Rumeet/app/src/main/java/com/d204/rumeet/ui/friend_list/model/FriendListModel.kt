package com.d204.rumeet.ui.friend_list.model

import com.d204.rumeet.domain.model.friend.FriendModel

data class FriendListModel(
    val userId : Int,
    val userProfileImg : String,
    val userNickname : String,
    val averagePace : String = ""
)

fun FriendModel.toUiModel() = FriendListModel(
    userId = this.userId,
    userProfileImg = this.profileImg,
    userNickname = this.nickname,
    averagePace = ""
)