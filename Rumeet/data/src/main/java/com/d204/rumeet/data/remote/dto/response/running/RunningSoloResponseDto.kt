package com.d204.rumeet.data.remote.dto.response.running

import com.d204.rumeet.domain.model.user.RunningSoloDomainModel
import com.google.gson.annotations.SerializedName

internal data class RunningSoloResponseDto(
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("date")
    val date: Long ?= 0L,
    @SerializedName("mode")
    val mode: Int ?= 0,
    @SerializedName("pace")
    val pace: List<Int>? = emptyList(),
    @SerializedName("partnerId")
    val partnerId: Int ?= 0,
    @SerializedName("state")
    val state: Int ?= 0,
    @SerializedName("userId")
    val userId: Int?=0
)

internal fun RunningSoloResponseDto.toDomain() = RunningSoloDomainModel(
    id = this.id ?: 0,
    date = this.date ?: 0,
    mode = this.mode ?: 0,
    pace = this.pace ?: emptyList(),
    partnerId = this.partnerId ?: 0,
    state = this.state ?: 0,
    userId = this.userId ?: 0
)