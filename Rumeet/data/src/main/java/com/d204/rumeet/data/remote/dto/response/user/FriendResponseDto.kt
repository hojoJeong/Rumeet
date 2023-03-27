package com.d204.rumeet.data.remote.dto.response.user

import com.d204.rumeet.domain.model.friend.FriendModel
import com.google.gson.annotations.SerializedName

internal data class FriendResponseDto(
    @SerializedName("userId")
    val userId : Int
)

internal fun FriendResponseDto.toDomainModel() = FriendModel(
    userId = this.userId
)