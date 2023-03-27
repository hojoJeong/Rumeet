package com.d204.rumeet.data.remote.dto.response.user

import com.d204.rumeet.domain.model.user.UserModel
import com.google.gson.annotations.SerializedName

internal data class UserResponseDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("age")
    val age: Int?,
    @SerializedName("gender")
    val gender: Int?,
    @SerializedName("profileImg")
    val profileImg: String?,
    @SerializedName("date")
    val date: Long?,
    @SerializedName("state")
    val state: Int?
)

internal fun UserResponseDto.toDomainModel() = UserModel(
    id = this.id ?: -1,
    email = this.email ?: "",
    nickname = this.nickname ?: "",
    age = this.age ?: -1,
    gender = this.gender ?: -1,
    profileImg = this.profileImg ?: "",
    state = state ?: 0
)