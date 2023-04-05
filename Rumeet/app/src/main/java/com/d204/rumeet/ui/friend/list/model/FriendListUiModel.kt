package com.d204.rumeet.ui.friend.list.model

import com.d204.rumeet.domain.model.friend.FriendListDomainModel
import com.d204.rumeet.domain.model.friend.FriendModel

data class FriendListUiModel(
    val userId : Int,
    val userProfileImg : String,
    val userNickname : String,
    val averagePace : String = "",
)

fun FriendModel.toUiModel() = FriendListUiModel(
    userId = this.userId,
    userProfileImg = this.profileImg,
    userNickname = this.nickname,
    averagePace = ""
)
