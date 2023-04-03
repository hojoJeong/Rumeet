package com.d204.rumeet.ui.friend

import com.d204.rumeet.domain.model.friend.FriendInfoDomainModel
import com.d204.rumeet.ui.friend.add.model.UserListUiModel
import com.d204.rumeet.ui.friend.list.model.FriendListUiModel
import com.d204.rumeet.util.toDistance
import com.d204.rumeet.util.toMinute
import com.d204.rumeet.util.toRecord

data class UserDialogModel(
    val id: Int,
    val userNickname: String,
    val userProfileImg: String,
    val totalKm: String = "",
    val totalTime: String = "",
    val pace: String = ""
)

fun FriendListUiModel.toUserDialogModel() = UserDialogModel(
    id = this.userId,
    userNickname = this.userNickname,
    userProfileImg = this.userProfileImg
)

fun UserListUiModel.toUserDialogModel() = UserDialogModel(
    id = this.id,
    userNickname = this.nickname,
    userProfileImg = this.profileImg,
    totalKm.toInt().toDistance(), totalTime.toMinute(), pace.toRecord()
)

fun FriendInfoDomainModel.toUserDialogModel() = UserDialogModel(
    id,
    nickname,
    profileImg, totalKm.toDistance(), totalTime.toRecord(), pace.toRecord()
)