package com.d204.rumeet.ui.mypage.model

import com.d204.rumeet.domain.model.user.ModifyUserDetailInfoDomainModel

data class UserDetailInfoUiModel(
    var id: Int = -1,
    var gender: Int = -1,
    var height: Float = 0f,
    var weight: Float = 0f,
    var age: Int = -1
)

fun UserDetailInfoUiModel.toDomainModel() = ModifyUserDetailInfoDomainModel(
    age, gender, height, id, weight
)
