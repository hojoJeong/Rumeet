package com.d204.rumeet.ui.friend.add.model

import com.d204.rumeet.domain.model.friend.FriendModel
import com.d204.rumeet.domain.model.user.UserModel
import com.d204.rumeet.ui.friend.list.model.FriendListUiModel

data class UserListUiModel(
    val userId : Int,
    val userProfileImg : String,
    val userNickname : String,
    val averagePace : String = ""
)

fun UserModel.toUiModel() = UserListUiModel(
    userId = this.id,
    userProfileImg = this.profileImg,
    userNickname = this.nickname,
    averagePace = ""
)