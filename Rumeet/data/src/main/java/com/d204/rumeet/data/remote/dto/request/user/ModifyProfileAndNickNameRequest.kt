package com.d204.rumeet.data.remote.dto.request.user

import com.google.gson.annotations.SerializedName
import java.io.File

data class ModifyProfileAndNickNameRequest(
    @SerializedName("user")
    val info: ModifyNickNameRequest,
    @SerializedName("profile_img")
    val profileImg: File?
)