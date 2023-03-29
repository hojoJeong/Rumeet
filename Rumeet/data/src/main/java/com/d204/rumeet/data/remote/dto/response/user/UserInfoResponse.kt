package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("age")
    val age: Int,
    @SerializedName("date")
    val date: Long,
    @SerializedName("email")
    val email: String,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("height")
    val height: Double,
    @SerializedName("id")
    val id: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("oauth")
    val oauth: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("profile")
    val profile: String?,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("state")
    val state: Int,
    @SerializedName("weight")
    val weight: Double
)