package com.d204.rumeet.data.remote.dto.response.user

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("age")
    val age: Int,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("profile")
    val profile: String?,
    @SerializedName("height")
    val height: Double,
    @SerializedName("weight")
    val weight: Double,
    @SerializedName("date")
    val date: Long,
    @SerializedName("state")
    val state: Int,
    @SerializedName("oauth")
    val oauth: String
)