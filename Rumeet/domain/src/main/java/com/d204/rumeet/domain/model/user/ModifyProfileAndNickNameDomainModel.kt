package com.d204.rumeet.domain.model.user

import java.io.File

data class ModifyProfileAndNickNameDomainModel(
    val id: Int,
    val curProfile: String?,
    val profile: File?,
    val name: String
)