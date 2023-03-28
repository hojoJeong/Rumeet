package com.d204.rumeet.ui.friend

import com.d204.rumeet.ui.friend.add.model.UserListUiModel
import com.d204.rumeet.ui.friend.list.model.FriendListUiModel

data class UserDialogModel(
    val id: Int,
    val userNickname: String,
    val userProfileImg: String
)

fun FriendListUiModel.toUserDialogModel() = UserDialogModel(
    id = this.userId,
    userNickname = this.userNickname,
    userProfileImg = this.userProfileImg
)

fun UserListUiModel.toUserDialogModel() = UserDialogModel(
    id = this.userId,
    userNickname = this.userNickname,
    userProfileImg = this.userProfileImg
)