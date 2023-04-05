package com.d204.rumeet.ui.friend.add.model

import com.d204.rumeet.domain.model.friend.FriendModel
import com.d204.rumeet.domain.model.user.UserModel
import com.d204.rumeet.ui.friend.list.model.FriendListUiModel

data class UserListUiModel(
    val id : Int,
    val nickname : String,
    val profileImg : String,
    val totalKm : Double,
    val totalTime : Long,
    val pace: Int
)

fun UserModel.toUiModel() = UserListUiModel(
    id, nickname, profileImg, totalKm, totalTime, pace
)