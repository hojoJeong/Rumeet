package com.d204.rumeet.data.remote.dto.request.user

import com.google.gson.annotations.SerializedName

data class ModifyNickNameRequest(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nickname")
    val name: String,
    @SerializedName("profile")
    val profile: String?
)