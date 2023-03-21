package com.d204.rumeet.data.remote.dto.request.auth

import com.google.gson.annotations.SerializedName

internal data class EmailLoginRequest(
    @SerializedName("email")
    val email : String,
    @SerializedName("password")
    val password : String
)