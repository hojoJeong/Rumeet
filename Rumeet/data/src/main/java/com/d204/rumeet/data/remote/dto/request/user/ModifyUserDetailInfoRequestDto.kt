package com.d204.rumeet.data.remote.dto.request.user

import com.d204.rumeet.domain.model.user.ModifyUserDetailInfoDomainModel
import com.google.gson.annotations.SerializedName

data class ModifyUserDetailInfoRequestDto(
    @SerializedName("age")
    val age: Int,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("height")
    val height: Float,
    @SerializedName("id")
    val id: Int,
    @SerializedName("weight")
    val weight: Float
)



