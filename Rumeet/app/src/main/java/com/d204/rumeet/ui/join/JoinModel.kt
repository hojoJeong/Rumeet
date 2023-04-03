package com.d204.rumeet.ui.join

import com.d204.rumeet.domain.model.user.ModifyProfileAndNickNameDomainModel
import java.io.File

data class JoinModel(
    var id: String = "",
    var socialJoinModel: SocialJoinModel? = null,
    var profileImg: File? = null,
    var nickname: String = "",
    var password: String = "",
    var weight: Float = 0f,
    var height: Float = 0f,
    var gender: Int = -1,
    var age: Int = -1
)

data class SocialJoinModel(
    val oauth: Long,
    val profileImgUrl: String
)

data class EditProfile(
    var id: Int = -1,
    var curProfile: String? = "",
    var editProfile: File? = null,
    var name: String = ""
)

fun EditProfile.toDoMainModel() = ModifyProfileAndNickNameDomainModel(
    id, curProfile, editProfile, name
)