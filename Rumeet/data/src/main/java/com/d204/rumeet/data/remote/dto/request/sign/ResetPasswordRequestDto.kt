package com.d204.rumeet.data.remote.dto.request.sign

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequestDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)