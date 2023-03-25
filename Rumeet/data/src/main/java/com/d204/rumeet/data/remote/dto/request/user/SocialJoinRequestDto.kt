package com.d204.rumeet.data.remote.dto.request.user

import com.google.gson.annotations.SerializedName

data class SocialJoinRequestDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("age")
    val age: Int,
    @SerializedName("height")
    val height: Float,
    @SerializedName("weight")
    val weight: Float,
    @SerializedName("oauth")
    val oauth : String,
    @SerializedName("date")
    val currentTime : Long
)