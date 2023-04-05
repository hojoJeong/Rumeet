package com.d204.rumeet.data.remote.dto.response.user

import com.d204.rumeet.domain.model.user.UserModel
import com.google.gson.annotations.SerializedName

internal data class UserResponseDto(
    @SerializedName("id")
    val userId: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImg")
    val profileImg: String?,
    @SerializedName("totalKm")
    val totalKm: Double?,
    @SerializedName("totalTime")
    val totalTime: Long?,
    @SerializedName("pace")
    val pace: Int?
)

internal fun UserResponseDto.toDomainModel() = UserModel(
    id = this.userId,
    nickname = this.nickname,
    profileImg = this.profileImg ?: "",
    totalKm = this.totalKm ?: 0.0,
    totalTime = this.totalTime ?: 0,
    pace = this.pace ?: 0
)
